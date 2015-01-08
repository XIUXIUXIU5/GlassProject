/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
//import com.google.glass.timeline.TimelineHelper;
//import com.google.glass.timeline.TimelineProvider;
//import com.google.glass.util.SettingsSecure;
//import com.google.googlex.glass.common.proto.MenuItem;
//import com.google.googlex.glass.common.proto.MenuValue;
//import com.google.googlex.glass.common.proto.TimelineItem;
import com.ozcanlab.activities.ImagerActivity;
import com.ozcanlab.application.RDTApplication;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import static com.ozcanlab.utils.Constants.*;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
//import com.google.android.glass.timeline.TimelineManager;

/**
 *
 * @author SaqibIsGreat
 */
public class TimelineUtils {

    /*
    public static void applyHomeCardValues(TimelineItem.Builder tibuilder, String html) {
        //pin status.
        tibuilder.setIsPinned(true);
        tibuilder.setPinTime(10); //unpinned pin time  	
        tibuilder.setTitle("RDT Home Card");
        //custom URL that this app will intercept.
        tibuilder.clearMenuItem();
        tibuilder.addMenuItem(
                MenuItem.newBuilder().setAction(MenuItem.Action.VIEW_WEBSITE).setId(UUID.randomUUID().toString())
                .setPayload(URI_FORM + "process/qr-code")
                .addValue(
                MenuValue.newBuilder().setDisplayName("Process")
                .setIconUrl("http://c.dryicons.com/images/icon_sets/stylistica_icons_set/png/64x64/magic_wand.png")
                .build()).build());

        tibuilder.addMenuItem(MenuItem.newBuilder().setAction(MenuItem.Action.DELETE).setId(UUID.randomUUID().toString()).build());

        //HTML
        tibuilder.setHtml(html);
    }

    public static void initHomeCard(Context ctx) {
        final TimelineHelper tlHelper = new TimelineHelper();
        ContentResolver cr = ctx.getContentResolver();
        //For some reason an TimelineHelper instance is required to call some methods.
        TimelineItem.Builder ntib = tlHelper.createTimelineItemBuilder(ctx, new SettingsSecure(cr));
        TimelineUtils.applyHomeCardValues(ntib, HOME_CARD_HTML);
        TimelineItem rdtHomeCard = ntib.build();
        ContentValues vals = TimelineHelper.toContentValues(rdtHomeCard);

        ((MyApplication) ctx.getApplicationContext()).setRdtHomeCard(rdtHomeCard);

        cr.insert(TimelineProvider.TIMELINE_URI, vals);
    }

    public static String pushNewProcessingCard(ImagerActivity aThis) {
        ContentResolver cr = aThis.getContentResolver();
        //For some reason an TimelineHelper instance is required to call some methods.
        final TimelineHelper tlHelper = new TimelineHelper();
        TimelineItem.Builder ntib = tlHelper.createTimelineItemBuilder(aThis, new SettingsSecure(cr));

        // Build card
        ntib.setTitle("RDT Home Card");
        //custom URL that this app will intercept.
        ntib.clearMenuItem();

        //HTML
        SimpleDateFormat format = new SimpleDateFormat("MM/dd hh:mm");
        ntib.setHtml(IN_PROGRESS_UPPER + format.format(new Date()) + IN_PROGRESS_LOWER);

        TimelineItem card = ntib.build();
        ContentValues vals = TimelineHelper.toContentValues(card);
        cr.insert(TimelineProvider.TIMELINE_URI, vals);

        return card.getId();
    }

    public static void setProcessingCardDone(String cardId, final ImagerActivity aThis) {
        final TimelineHelper tlHelper = new TimelineHelper();
        final TimelineItem it = tlHelper.queryTimelineItem(aThis.getContentResolver(), cardId);

        if (it == null) {
            Log.d(APP_TAG, "Timeline Item was null ...");
            return;
        }

        TimelineHelper.Update updater = new TimelineHelper.Update() {
            @Override
            public TimelineItem onExecute() {

                TimelineItem.Builder builder = TimelineItem.newBuilder(it);
                SimpleDateFormat format = new SimpleDateFormat("MM/dd hh:mm");
                builder.setTitle("RDT Processing Card");
                //Clear
                builder.clearHtml();
                builder.clearMenuItem();

                // Add Delete option
                builder.addMenuItem(MenuItem.newBuilder().setAction(MenuItem.Action.DELETE).setId(UUID.randomUUID().toString()).build());
                //HTML
                builder.setHtml(PROCESSED_OK_UPPER + format.format(new Date()) + PROCESSED_OK_LOWER);
                return tlHelper.updateTimelineItem(aThis, builder.build(), null, true, false);
            }
        };

        (new TimelineHelper()).atomicUpdateTimelineItemAsync(updater);
    }
    */
}
