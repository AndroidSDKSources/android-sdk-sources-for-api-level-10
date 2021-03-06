/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.content.res;

import android.text.*;
import android.text.style.*;
import android.util.Config;
import android.util.Log;
import android.util.SparseArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.android.internal.util.XmlUtils;

/**
 * Conveniences for retrieving data out of a compiled string resource.
 *
 * {@hide}
 */
final class StringBlock {
    private static final String TAG = "AssetManager";
    private static final boolean localLOGV = Config.LOGV || false;

    private final int mNative;
    private final boolean mUseSparse;
    private final boolean mOwnsNative;
    private CharSequence[] mStrings;
    private SparseArray<CharSequence> mSparseStrings;
    StyleIDs mStyleIDs = null;

    public StringBlock(byte[] data, boolean useSparse) {
        mNative = nativeCreate(data, 0, data.length);
        mUseSparse = useSparse;
        mOwnsNative = true;
        if (localLOGV) Log.v(TAG, "Created string block " + this
                + ": " + nativeGetSize(mNative));
    }

    public StringBlock(byte[] data, int offset, int size, boolean useSparse) {
        mNative = nativeCreate(data, offset, size);
        mUseSparse = useSparse;
        mOwnsNative = true;
        if (localLOGV) Log.v(TAG, "Created string block " + this
                + ": " + nativeGetSize(mNative));
    }

    public CharSequence get(int idx) {
        synchronized (this) {
            if (mStrings != null) {
                CharSequence res = mStrings[idx];
                if (res != null) {
                    return res;
                }
            } else if (mSparseStrings != null) {
                CharSequence res = mSparseStrings.get(idx);
                if (res != null) {
                    return res;
                }
            } else {
                final int num = nativeGetSize(mNative);
                if (mUseSparse && num > 250) {
                    mSparseStrings = new SparseArray<CharSequence>();
                } else {
                    mStrings = new CharSequence[num];
                }
            }
            String str = nativeGetString(mNative, idx);
            CharSequence res = str;
            int[] style = nativeGetStyle(mNative, idx);
            if (localLOGV) Log.v(TAG, "Got string: " + str);
            if (localLOGV) Log.v(TAG, "Got styles: " + style);
            if (style != null) {
                if (mStyleIDs == null) {
                    mStyleIDs = new StyleIDs();
                    mStyleIDs.boldId = nativeIndexOfString(mNative, "b");
                    mStyleIDs.italicId = nativeIndexOfString(mNative, "i");
                    mStyleIDs.underlineId = nativeIndexOfString(mNative, "u");
                    mStyleIDs.ttId = nativeIndexOfString(mNative, "tt");
                    mStyleIDs.bigId = nativeIndexOfString(mNative, "big");
                    mStyleIDs.smallId = nativeIndexOfString(mNative, "small");
                    mStyleIDs.supId = nativeIndexOfString(mNative, "sup");
                    mStyleIDs.subId = nativeIndexOfString(mNative, "sub");
                    mStyleIDs.strikeId = nativeIndexOfString(mNative, "strike");
                    mStyleIDs.listItemId = nativeIndexOfString(mNative, "li");
                    mStyleIDs.marqueeId = nativeIndexOfString(mNative, "marquee");

                    if (localLOGV) Log.v(TAG, "BoldId=" + mStyleIDs.boldId
                            + ", ItalicId=" + mStyleIDs.italicId
                            + ", UnderlineId=" + mStyleIDs.underlineId);
                }

                res = applyStyles(str, style, mStyleIDs);
            }
            if (mStrings != null) mStrings[idx] = res;
            else mSparseStrings.put(idx, res);
            return res;
        }
    }

    protected void finalize() throws Throwable {
        if (mOwnsNative) {
            nativeDestroy(mNative);
        }
    }

    static final class StyleIDs {
        private int boldId;
        private int italicId;
        private int underlineId;
        private int ttId;
        private int bigId;
        private int smallId;
        private int subId;
        private int supId;
        private int strikeId;
        private int listItemId;
        private int marqueeId;
    }

    private CharSequence applyStyles(String str, int[] style, StyleIDs ids) {
        if (style.length == 0)
            return str;

        SpannableString buffer = new SpannableString(str);
        int i=0;
        while (i < style.length) {
            int type = style[i];
            if (localLOGV) Log.v(TAG, "Applying style span id=" + type
                    + ", start=" + style[i+1] + ", end=" + style[i+2]);


            if (type == ids.boldId) {
                buffer.setSpan(new StyleSpan(Typeface.BOLD),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.italicId) {
                buffer.setSpan(new StyleSpan(Typeface.ITALIC),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.underlineId) {
                buffer.setSpan(new UnderlineSpan(),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.ttId) {
                buffer.setSpan(new TypefaceSpan("monospace"),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.bigId) {
                buffer.setSpan(new RelativeSizeSpan(1.25f),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.smallId) {
                buffer.setSpan(new RelativeSizeSpan(0.8f),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.subId) {
                buffer.setSpan(new SubscriptSpan(),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.supId) {
                buffer.setSpan(new SuperscriptSpan(),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.strikeId) {
                buffer.setSpan(new StrikethroughSpan(),
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (type == ids.listItemId) {
                addParagraphSpan(buffer, new BulletSpan(10),
                                style[i+1], style[i+2]+1);
            } else if (type == ids.marqueeId) {
                buffer.setSpan(TextUtils.TruncateAt.MARQUEE,
                               style[i+1], style[i+2]+1,
                               Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else {
                String tag = nativeGetString(mNative, type);

                if (tag.startsWith("font;")) {
                    String sub;

                    sub = subtag(tag, ";height=");
                    if (sub != null) {
                        int size = Integer.parseInt(sub);
                        addParagraphSpan(buffer, new Height(size),
                                       style[i+1], style[i+2]+1);
                    }

                    sub = subtag(tag, ";size=");
                    if (sub != null) {
                        int size = Integer.parseInt(sub);
                        buffer.setSpan(new AbsoluteSizeSpan(size, true),
                                       style[i+1], style[i+2]+1,
                                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    sub = subtag(tag, ";fgcolor=");
                    if (sub != null) {
                        int color = XmlUtils.convertValueToUnsignedInt(sub, -1);
                        buffer.setSpan(new ForegroundColorSpan(color),
                                       style[i+1], style[i+2]+1,
                                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    sub = subtag(tag, ";bgcolor=");
                    if (sub != null) {
                        int color = XmlUtils.convertValueToUnsignedInt(sub, -1);
                        buffer.setSpan(new BackgroundColorSpan(color),
                                       style[i+1], style[i+2]+1,
                                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (tag.startsWith("a;")) {
                    String sub;

                    sub = subtag(tag, ";href=");
                    if (sub != null) {
                        buffer.setSpan(new URLSpan(sub),
                                       style[i+1], style[i+2]+1,
                                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (tag.startsWith("annotation;")) {
                    int len = tag.length();
                    int next;

                    for (int t = tag.indexOf(';'); t < len; t = next) {
                        int eq = tag.indexOf('=', t);
                        if (eq < 0) {
                            break;
                        }

                        next = tag.indexOf(';', eq);
                        if (next < 0) {
                            next = len;
                        }

                        String key = tag.substring(t + 1, eq);
                        String value = tag.substring(eq + 1, next);

                        buffer.setSpan(new Annotation(key, value),
                                       style[i+1], style[i+2]+1,
                                       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            i += 3;
        }
        return new SpannedString(buffer);
    }

    /**
     * If a translator has messed up the edges of paragraph-level markup,
     * fix it to actually cover the entire paragraph that it is attached to
     * instead of just whatever range they put it on.
     */
    private static void addParagraphSpan(Spannable buffer, Object what,
                                         int start, int end) {
        int len = buffer.length();

        if (start != 0 && start != len && buffer.charAt(start - 1) != '\n') {
            for (start--; start > 0; start--) {
                if (buffer.charAt(start - 1) == '\n') {
                    break;
                }
            }
        }

        if (end != 0 && end != len && buffer.charAt(end - 1) != '\n') {
            for (end++; end < len; end++) {
                if (buffer.charAt(end - 1) == '\n') {
                    break;
                }
            }
        }

        buffer.setSpan(what, start, end, Spannable.SPAN_PARAGRAPH);
    }

    private static String subtag(String full, String attribute) {
        int start = full.indexOf(attribute);
        if (start < 0) {
            return null;
        }

        start += attribute.length();
        int end = full.indexOf(';', start);

        if (end < 0) {
            return full.substring(start);
        } else {
            return full.substring(start, end);
        }
    }

    /**
     * Forces the text line to be the specified height, shrinking/stretching
     * the ascent if possible, or the descent if shrinking the ascent further
     * will make the text unreadable.
     */
    private static class Height implements LineHeightSpan.WithDensity {
        private int mSize;
        private static float sProportion = 0;

        public Height(int size) {
            mSize = size;
        }

        public void chooseHeight(CharSequence text, int start, int end,
                                 int spanstartv, int v,
                                 Paint.FontMetricsInt fm) {
            // Should not get called, at least not by StaticLayout.
            chooseHeight(text, start, end, spanstartv, v, fm, null);
        }

        public void chooseHeight(CharSequence text, int start, int end,
                                 int spanstartv, int v,
                                 Paint.FontMetricsInt fm, TextPaint paint) {
            int size = mSize;
            if (paint != null) {
                size *= paint.density;
            }

            if (fm.bottom - fm.top < size) {
                fm.top = fm.bottom - size;
                fm.ascent = fm.ascent - size;
            } else {
                if (sProportion == 0) {
                    /*
                     * Calculate what fraction of the nominal ascent
                     * the height of a capital letter actually is,
                     * so that we won't reduce the ascent to less than
                     * that unless we absolutely have to.
                     */

                    Paint p = new Paint();
                    p.setTextSize(100);
                    Rect r = new Rect();
                    p.getTextBounds("ABCDEFG", 0, 7, r);

                    sProportion = (r.top) / p.ascent();
                }

                int need = (int) Math.ceil(-fm.top * sProportion);

                if (size - fm.descent >= need) {
                    /*
                     * It is safe to shrink the ascent this much.
                     */

                    fm.top = fm.bottom - size;
                    fm.ascent = fm.descent - size;
                } else if (size >= need) {
                    /*
                     * We can't show all the descent, but we can at least
                     * show all the ascent.
                     */

                    fm.top = fm.ascent = -need;
                    fm.bottom = fm.descent = fm.top + size;
                } else {
                    /*
                     * Show as much of the ascent as we can, and no descent.
                     */

                    fm.top = fm.ascent = -size;
                    fm.bottom = fm.descent = 0;
                }
            }
        }
    }

    /**
     * Create from an existing string block native object.  This is
     * -extremely- dangerous -- only use it if you absolutely know what you
     *  are doing!  The given native object must exist for the entire lifetime
     *  of this newly creating StringBlock.
     */
    StringBlock(int obj, boolean useSparse) {
        mNative = obj;
        mUseSparse = useSparse;
        mOwnsNative = false;
        if (localLOGV) Log.v(TAG, "Created string block " + this
                + ": " + nativeGetSize(mNative));
    }

    private static final native int nativeCreate(byte[] data,
                                                 int offset,
                                                 int size);
    private static final native int nativeGetSize(int obj);
    private static final native String nativeGetString(int obj, int idx);
    private static final native int[] nativeGetStyle(int obj, int idx);
    private static final native int nativeIndexOfString(int obj, String str);
    private static final native void nativeDestroy(int obj);
}
