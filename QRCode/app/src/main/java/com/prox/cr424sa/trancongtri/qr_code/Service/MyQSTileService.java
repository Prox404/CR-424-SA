package com.prox.cr424sa.trancongtri.qr_code.Service;

import android.content.Intent;
import android.service.quicksettings.TileService;

import com.prox.cr424sa.trancongtri.qr_code.ScannerActivity;

public class MyQSTileService extends TileService {
    // Called when the user adds your tile.
    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    // Called when your app can update your tile.
    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    // Called when your app can no longer update your tile.
    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    // Called when the user taps on your tile in an active or inactive state.
    @Override
    public void onClick() {
        super.onClick();

        Intent intent = new Intent(this, ScannerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the activity
        startActivityAndCollapse(intent);
    }

    // Called when the user removes your tile.
    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }
}
