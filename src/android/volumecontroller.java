package kiosk.plugin;

import android.media.RingtoneManager;
import android.media.AudioManager;
import android.net.Uri;
import android.media.Ringtone;
import android.content.Context;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */
public class volumecontroller extends CordovaPlugin {

    private static final String ACTION_BEEP           = "beep";
    private static final String ACTION_SET_VOLUME           = "setVolume";
    private static final String ACTION_GET_MAX_VOLUME           = "getMaxVolume";
    private static final String ACTION_GET_VOLUME           = "getVolume";
    private static final long BEEP_TIMEOUT   = 5000;
    private static final long BEEP_WAIT_TINE = 100;
    private CallbackContext callback = null;

    @Override
    public boolean execute(String action, JSONArray args,final CallbackContext callbackContext) throws JSONException {
        this.callback = callbackContext;

        if (action.equals(ACTION_BEEP)) {
            this.beep(args.getLong(0));
            return true;
        }else if(action.equals(ACTION_SET_VOLUME)){
             this.setVolume(args.getInt(0));
            return true;
        }
        else if(action.equals(ACTION_GET_MAX_VOLUME)){
            this.getMaxVolume();
            return true;
        }else if(action.equals(ACTION_GET_VOLUME)){
            this.getVolume();
            return true;
        }
        else return false;
    }

    /**
     * Beep plays the default notification ringtone.
     *
     * @param count     Number of times to play notification
     */
    public void beep(final long count) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone notification = RingtoneManager.getRingtone(cordova.getActivity().getBaseContext(), ringtone);

                // If phone is not set to silent mode
                if (notification != null) {
                    for (long i = 0; i < count; ++i) {
                        notification.play();
                        long timeout = BEEP_TIMEOUT;
                        while (notification.isPlaying() && (timeout > 0)) {
                            timeout = timeout - BEEP_WAIT_TINE;
                            try {
                                Thread.sleep(BEEP_WAIT_TINE);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            }
        });
    }
    public void setVolume(final int volumeLevel) {
        try {
            AudioManager audioManager = (AudioManager) this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volumeLevel, AudioManager.FLAG_SHOW_UI);
            this.callback.success();
        } catch (Exception e) {
            this.callback.error(e.toString());
        }
    }
    public void getMaxVolume(){
        try {
            AudioManager audioManager = (AudioManager) this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
            this.callback.success(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
            // PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
            // pluginResult.setKeepCallback(true); // keep callback
            // this.callback.sendPluginResult(pluginResult);
        } catch (Exception e) {
            this.callback.error(e.toString());
        }
    }
    public void getVolume(){
        try {
            AudioManager audioManager = (AudioManager) this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
            this.callback.success(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
            // PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
            // pluginResult.setKeepCallback(true); // keep callback
            // this.callback.sendPluginResult(pluginResult);
        } catch (Exception e) {
            this.callback.error(e.toString());
        }
    }
}
