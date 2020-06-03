package ankit.oromap.helpo.extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class br extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"called",Toast.LENGTH_LONG).show();
        context.startService(new Intent(context,emerservice.class));
        cl(emerservice.class);
    }
    public void cl(Class<?> ser)
    {

    }
}
