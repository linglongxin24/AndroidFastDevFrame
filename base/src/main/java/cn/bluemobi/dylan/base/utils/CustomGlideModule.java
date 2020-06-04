package cn.bluemobi.dylan.base.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


/**
 * @author YDL
 * @version 1.0
 * @date 2020/06/04/10:56
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
