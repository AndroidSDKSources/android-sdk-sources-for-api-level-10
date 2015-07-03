package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
// BEGIN android-removed
// import org.bouncycastle.crypto.digests.MD2Digest;
// import org.bouncycastle.crypto.digests.MD4Digest;
// END android-removed
import org.bouncycastle.crypto.digests.MD5Digest;
// BEGIN android-removed
// import org.bouncycastle.crypto.digests.RIPEMD128Digest;
// import org.bouncycastle.crypto.digests.RIPEMD160Digest;
// END android-removed
import org.bouncycastle.crypto.digests.SHA1Digest;
// BEGIN android-removed
// import org.bouncycastle.crypto.digests.SHA224Digest;
// END android-removed
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
// BEGIN android-removed
// import org.bouncycastle.crypto.digests.TigerDigest;
// END android-removed
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
// BEGIN android-removed
// import org.bouncycastle.crypto.engines.RC2Engine;
// import org.bouncycastle.crypto.engines.RC532Engine;
// import org.bouncycastle.crypto.engines.SkipjackEngine;
// END android-removed
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
// BEGIN android-removed
// import org.bouncycastle.crypto.macs.CFBBlockCipherMac;
// import org.bouncycastle.crypto.macs.GOST28147Mac;
// END android-removed
import org.bouncycastle.crypto.macs.HMac;
// BEGIN android-removed
// import org.bouncycastle.crypto.macs.ISO9797Alg3Mac;
// import org.bouncycastle.crypto.macs.OldHMac;
// import org.bouncycastle.crypto.macs.VMPCMac;
// END android-removed
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import javax.crypto.MacSpi;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class JCEMac
    extends MacSpi implements PBE
{
    private Mac macEngine;

    private int                     pbeType = PKCS12;
    private int                     pbeHash = SHA1;
    private int                     keySize = 160;

    protected JCEMac(
        Mac macEngine)
    {
        this.macEngine = macEngine;
    }

    protected JCEMac(
        Mac macEngine,
        int pbeType,
        int pbeHash,
        int keySize)
    {
        this.macEngine = macEngine;
        this.pbeType = pbeType;
        this.pbeHash = pbeHash;
        this.keySize = keySize;
    }

    protected void engineInit(
        Key                     key,
        AlgorithmParameterSpec  params)
        throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        CipherParameters        param;

        if (key == null)
        {
            throw new InvalidKeyException("key is null");
        }
        
        if (key instanceof JCEPBEKey)
        {
            JCEPBEKey   k = (JCEPBEKey)key;
            
            if (k.getParam() != null)
            {
                param = k.getParam();
            }
            else if (params instanceof PBEParameterSpec)
            {
                param = PBE.Util.makePBEMacParameters(k, params);
            }
            else
            {
                throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
            }
        }
        else if (params instanceof IvParameterSpec)
        {
            param = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)params).getIV());
        }
        else if (params == null)
        {
            param = new KeyParameter(key.getEncoded());
        }
        else
        {
            throw new InvalidAlgorithmParameterException("unknown parameter type.");
        }

        macEngine.init(param);
    }

    protected int engineGetMacLength() 
    {
        return macEngine.getMacSize();
    }

    protected void engineReset() 
    {
        macEngine.reset();
    }

    protected void engineUpdate(
        byte    input) 
    {
        macEngine.update(input);
    }

    protected void engineUpdate(
        byte[]  input,
        int     offset,
        int     len) 
    {
        macEngine.update(input, offset, len);
    }

    protected byte[] engineDoFinal() 
    {
        byte[]  out = new byte[engineGetMacLength()];

        macEngine.doFinal(out, 0);

        return out;
    }

    /**
     * the classes that extend directly off us.
     */

    // BEGIN android-removed
    // /**
    //  * DES
    //  */
    // public static class DES
    //     extends JCEMac
    // {
    //     public DES()
    //     {
    //         super(new CBCBlockCipherMac(new DESEngine()));
    //     }
    // }
    //
    // /**
    //  * DESede
    //  */
    // public static class DESede
    //     extends JCEMac
    // {
    //     public DESede()
    //     {
    //         super(new CBCBlockCipherMac(new DESedeEngine()));
    //     }
    // }
    //
    // /**
    //  * SKIPJACK
    //  */
    // public static class Skipjack
    //     extends JCEMac
    // {
    //     public Skipjack()
    //     {
    //         super(new CBCBlockCipherMac(new SkipjackEngine()));
    //     }
    // }
    //
    // /**
    //  * RC2
    //  */
    // public static class RC2
    //     extends JCEMac
    // {
    //     public RC2()
    //     {
    //         super(new CBCBlockCipherMac(new RC2Engine()));
    //     }
    // }
    //
    // /**
    //  * RC5
    //  */
    // public static class RC5
    //     extends JCEMac
    // {
    //     public RC5()
    //     {
    //         super(new CBCBlockCipherMac(new RC532Engine()));
    //     }
    // }
    //
    // /**
    //  * GOST28147
    //  */
    // public static class GOST28147
    //     extends JCEMac
    // {
    //     public GOST28147()
    //     {
    //         super(new GOST28147Mac());
    //     }
    // }
    //
    // /**
    //  * VMPC
    //  */
    // public static class VMPC
    //     extends JCEMac
    // {
    //     public VMPC()
    //     {
    //         super(new VMPCMac());
    //     }
    // }
    //
    // /**
    //  * DES
    //  */
    // public static class DESCFB8
    //     extends JCEMac
    // {
    //     public DESCFB8()
    //     {
    //         super(new CFBBlockCipherMac(new DESEngine()));
    //     }
    // }
    //
    // /**
    //  * DESede
    //  */
    // public static class DESedeCFB8
    //     extends JCEMac
    // {
    //     public DESedeCFB8()
    //     {
    //         super(new CFBBlockCipherMac(new DESedeEngine()));
    //     }
    // }
    //
    // /**
    //  * SKIPJACK
    //  */
    // public static class SkipjackCFB8
    //     extends JCEMac
    // {
    //     public SkipjackCFB8()
    //     {
    //         super(new CFBBlockCipherMac(new SkipjackEngine()));
    //     }
    // }
    //
    // /**
    //  * RC2CFB8
    //  */
    // public static class RC2CFB8
    //     extends JCEMac
    // {
    //     public RC2CFB8()
    //     {
    //         super(new CFBBlockCipherMac(new RC2Engine()));
    //     }
    // }
    //
    // /**
    //  * RC5CFB8
    //  */
    // public static class RC5CFB8
    //     extends JCEMac
    // {
    //     public RC5CFB8()
    //     {
    //         super(new CFBBlockCipherMac(new RC532Engine()));
    //     }
    // }
    //
    //
    // /**
    //  * DESede64
    //  */
    // public static class DESede64
    //     extends JCEMac
    // {
    //     public DESede64()
    //     {
    //         super(new CBCBlockCipherMac(new DESedeEngine(), 64));
    //     }
    // }
    //
    // /**
    //  * DESede64with7816-4Padding
    //  */
    // public static class DESede64with7816d4
    //     extends JCEMac
    // {
    //     public DESede64with7816d4()
    //     {
    //         super(new CBCBlockCipherMac(new DESedeEngine(), 64, new ISO7816d4Padding()));
    //     }
    // }
    //
    // /**
    //  * DES9797Alg3with7816-4Padding
    //  */
    // public static class DES9797Alg3with7816d4
    //     extends JCEMac
    // {
    //     public DES9797Alg3with7816d4()
    //     {
    //         super(new ISO9797Alg3Mac(new DESEngine(), new ISO7816d4Padding()));
    //     }
    // }
    //
    // /**
    //  * DES9797Alg3
    //  */
    // public static class DES9797Alg3
    //     extends JCEMac
    // {
    //     public DES9797Alg3()
    //     {
    //         super(new ISO9797Alg3Mac(new DESEngine()));
    //     }
    // }
    //
    // /**
    //  * MD2 HMac
    //  */
    // public static class MD2
    //     extends JCEMac
    // {
    //     public MD2()
    //     {
    //         super(new HMac(new MD2Digest()));
    //     }
    // }
    //
    // /**
    //  * MD4 HMac
    //  */
    // public static class MD4
    //     extends JCEMac
    // {
    //     public MD4()
    //     {
    //         super(new HMac(new MD4Digest()));
    //     }
    // }
    // END android-removed
    
    /**
     * MD5 HMac
     */
    public static class MD5
        extends JCEMac
    {
        public MD5()
        {
            super(new HMac(new MD5Digest()));
        }
    }
    
    /**
     * SHA1 HMac
     */
    public static class SHA1
        extends JCEMac
    {
        public SHA1()
        {
            super(new HMac(new SHA1Digest()));
        }
    }
    
    // BEGIN android-removed
    // /**
    //  * SHA-224 HMac
    //  */
    // public static class SHA224
    //     extends JCEMac
    // {
    //     public SHA224()
    //     {
    //         super(new HMac(new SHA224Digest()));
    //     }
    // }
    // END android-removed
    
    /**
     * SHA-256 HMac
     */
    public static class SHA256
        extends JCEMac
    {
        public SHA256()
        {
            super(new HMac(new SHA256Digest()));
        }
    }
    
    /**
     * SHA-384 HMac
     */
    public static class SHA384
        extends JCEMac
    {
        public SHA384()
        {
            super(new HMac(new SHA384Digest()));
        }
    }
    
    // BEGIN android-removed
    // public static class OldSHA384
    //     extends JCEMac
    // {
    //     public OldSHA384()
    //     {
    //         super(new OldHMac(new SHA384Digest()));
    //     }
    // }
    // END android-removed
    
    /**
     * SHA-512 HMac
     */
    public static class SHA512
        extends JCEMac
    {
        public SHA512()
        {
            super(new HMac(new SHA512Digest()));
        }
    }
    
    // BEGIN android-removed
    // /**
    //  * SHA-512 HMac
    //  */
    // public static class OldSHA512
    //     extends JCEMac
    // {
    //     public OldSHA512()
    //     {
    //         super(new OldHMac(new SHA512Digest()));
    //     }
    // }
    //
    // /**
    //  * RIPEMD128 HMac
    //  */
    // public static class RIPEMD128
    //     extends JCEMac
    // {
    //     public RIPEMD128()
    //     {
    //        super(new HMac(new RIPEMD128Digest()));
    //     }
    // }
    //
    // /**
    //  * RIPEMD160 HMac
    //  */
    // public static class RIPEMD160
    //     extends JCEMac
    // {
    //     public RIPEMD160()
    //     {
    //        super(new HMac(new RIPEMD160Digest()));
    //     }
    // }
    //
    // /**
    //  * Tiger HMac
    //  */
    // public static class Tiger
    //     extends JCEMac
    // {
    //     public Tiger()
    //     {
    //         super(new HMac(new TigerDigest()));
    //     }
    // }
    //
    // //
    // // PKCS12 states that the same algorithm should be used
    // // for the key generation as is used in the HMAC, so that
    // // is what we do here.
    // //
    //
    // /**
    //  * PBEWithHmacRIPEMD160
    //  */
    // public static class PBEWithRIPEMD160
    //     extends JCEMac
    // {
    //     public PBEWithRIPEMD160()
    //     {
    //         super(new HMac(new RIPEMD160Digest()), PKCS12, RIPEMD160, 160);
    //     }
    // }
    // END android-removed
    
    /**
     * PBEWithHmacSHA
     */
    public static class PBEWithSHA
        extends JCEMac
    {
        public PBEWithSHA()
        {
            super(new HMac(new SHA1Digest()), PKCS12, SHA1, 160);
        }
    }
    
    // BEGIN android-removed
    //  /**
    //   * PBEWithHmacTiger
    //   */
    // public static class PBEWithTiger
    //     extends JCEMac
    // {
    //     public PBEWithTiger()
    //     {
    //         super(new HMac(new TigerDigest()), PKCS12, TIGER, 192);
    //     }
    // }
    // END android-removed
}
