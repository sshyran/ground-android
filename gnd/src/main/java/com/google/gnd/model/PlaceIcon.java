/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gnd.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gnd.R;
import com.google.gnd.view.util.ViewUtil;

public class PlaceIcon {
  private final Context context;
  private final BitmapDrawable drawable;
  private int color;

  public PlaceIcon(Context context, String iconId, int color) {
    this.context = context;
    String resourceName = "ic_marker_" + iconId.replace("-", "_");
    int resourceId =
        context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    drawable = (BitmapDrawable) ContextCompat.getDrawable(context, resourceId);
    this.color = color;
  }

  // TODO: Cache tinted bitmaps.
  public BitmapDescriptor getBitmap() {
    Bitmap tintedBitmap = ViewUtil.tintBitmap(drawable.getBitmap(), color);
    return BitmapDescriptorFactory.fromBitmap(tintedBitmap);
  }

  // TODO: Cache tinted bitmaps.
  public BitmapDescriptor getGreyBitmap() {
    Bitmap tintedBitmap =
        ViewUtil.tintBitmap(
            drawable.getBitmap(), context.getResources().getColor(R.color.colorGrey300));
    return BitmapDescriptorFactory.fromBitmap(tintedBitmap);
  }


  // TODO: Cache tinted bitmaps.
  public BitmapDescriptor getWhiteBitmap() {
    Bitmap tintedBitmap =
        ViewUtil.tintBitmap(
            drawable.getBitmap(), context.getResources().getColor(R.color.colorWhite));
    return BitmapDescriptorFactory.fromBitmap(tintedBitmap);
  }
}
