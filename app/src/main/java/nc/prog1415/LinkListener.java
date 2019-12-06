package nc.prog1415;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

public class LinkListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View view) {
        String url = "https://" + ((TextView)view).getText();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        for(Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper)context).getBaseContext())
        {
            if (context instanceof Activity) {
                ((Activity)context).startActivity(i);
                break;
            }
        }
        return true;
    }
}
