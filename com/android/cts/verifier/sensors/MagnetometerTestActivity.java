/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.cts.verifier.sensors;

import com.android.cts.verifier.PassFailButtons;
import com.android.cts.verifier.R;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * CTS Verifier case for verifying correct integration of accelerometer.
 * Displays a wedge using OpenGL that, on a correctly-integrated device, always
 * points down.
 */
public class MagnetometerTestActivity extends PassFailButtons.Activity {
    private GLSurfaceView mGLSurfaceView;

    private AccelerometerTestRenderer mListener;

    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        mSensorManager = (SensorManager) getApplicationContext().getSystemService(
                Context.SENSOR_SERVICE);
        AccelerometerTestRenderer renderer = new MagnetometerTestRenderer(this);
        mListener = renderer;

        setContentView(R.layout.pass_fail_gl);
        setInfoResources(R.string.snsr_mag_test, R.string.snsr_mag_test_info, -1);
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        mGLSurfaceView.setRenderer(renderer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mListener);
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mListener, mSensorManager.getSensorList(
                Sensor.TYPE_MAGNETIC_FIELD).get(0), SensorManager.SENSOR_DELAY_UI);
        mGLSurfaceView.onResume();
    }
}
