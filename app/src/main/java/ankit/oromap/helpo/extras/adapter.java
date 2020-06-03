package ankit.oromap.helpo.extras;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



public class adapter extends FragmentPagerAdapter {
    Handler pager_tasks;
    @Override
    public int getCount() {
        return 2;
    }
    public adapter(FragmentManager fm, Handler pager_taks)
    {
        super(fm);
        this.pager_tasks = pager_taks;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if(position==0)
        {
            f = new pager_views(1,pager_tasks);
        }
        else if(position==1)
        {
            f = new pager_views(2,pager_tasks);
        }
        return f;
    }
}
