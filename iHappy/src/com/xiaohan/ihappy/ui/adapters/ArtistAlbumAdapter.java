
package com.xiaohan.ihappy.ui.adapters;

import static com.xiaohan.ihappy.Constants.SIZE_THUMB;
import static com.xiaohan.ihappy.Constants.SRC_FIRST_AVAILABLE;
import static com.xiaohan.ihappy.Constants.TYPE_ALBUM;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.xiaohan.ihappy.R;
import com.xiaohan.ihappy.cache.ImageInfo;
import com.xiaohan.ihappy.cache.ImageProvider;
import com.xiaohan.ihappy.helpers.utils.MusicUtils;
import com.xiaohan.ihappy.ui.fragments.list.ArtistAlbumsFragment;
import com.xiaohan.ihappy.views.ViewHolderList;

import java.lang.ref.WeakReference;

/**
 * @author Andrew Neal
 */
public class ArtistAlbumAdapter extends SimpleCursorAdapter {

    private AnimationDrawable mPeakOneAnimation, mPeakTwoAnimation;

    private WeakReference<ViewHolderList> holderReference;

    private Context mContext;
    
    private ImageProvider mImageProvider;

    public ArtistAlbumAdapter(Context context, int layout, Cursor c, String[] from, int[] to,
            int flags) {
        super(context, layout, c, from, to, flags);
    	mContext = context;
    	mImageProvider = ImageProvider.getInstance( (Activity) mContext );
    }

    /**
     * Used to quickly our the ContextMenu
     */
    private final View.OnClickListener showContextMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.showContextMenu();
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        Cursor mCursor = (Cursor) getItem(position);
        // ViewHolderList
        ViewHolderList viewholder;

        if (view != null) {

            viewholder = new ViewHolderList(view);
            holderReference = new WeakReference<ViewHolderList>(viewholder);
            view.setTag(holderReference.get());

        } else {
            viewholder = (ViewHolderList)convertView.getTag();
        }

        // Album name
        String albumName = mCursor.getString(ArtistAlbumsFragment.mAlbumNameIndex);
        holderReference.get().mViewHolderLineOne.setText(albumName);

        // Artist name
        String artistName = mCursor.getString(ArtistAlbumsFragment.mArtistNameIndex);
        
        String albumId = mCursor.getString(ArtistAlbumsFragment.mAlbumIdIndex);

        ImageInfo mInfo = new ImageInfo();
        mInfo.type = TYPE_ALBUM;
        mInfo.size = SIZE_THUMB;
        mInfo.source = SRC_FIRST_AVAILABLE;
        mInfo.data = new String[]{ albumId , artistName, albumName };
        
        mImageProvider.loadImage( viewholder.mViewHolderImage, mInfo );        

        // Number of songs
        int songs_plural = mCursor.getInt(ArtistAlbumsFragment.mSongCountIndex);
        String numSongs = MusicUtils.makeAlbumsLabel(mContext, 0, songs_plural, true);
        holderReference.get().mViewHolderLineTwo.setText(numSongs);


        holderReference.get().mQuickContext.setOnClickListener(showContextMenu);

        // Now playing indicator
        long currentalbumid = MusicUtils.getCurrentAlbumId();
        long albumid = mCursor.getLong(ArtistAlbumsFragment.mAlbumIdIndex);
        if (currentalbumid == albumid) {
            holderReference.get().mPeakOne.setImageResource(R.anim.peak_meter_1);
            holderReference.get().mPeakTwo.setImageResource(R.anim.peak_meter_2);
            mPeakOneAnimation = (AnimationDrawable)holderReference.get().mPeakOne.getDrawable();
            mPeakTwoAnimation = (AnimationDrawable)holderReference.get().mPeakTwo.getDrawable();
            try {
                if (MusicUtils.mService.isPlaying()) {
                    mPeakOneAnimation.start();
                    mPeakTwoAnimation.start();
                } else {
                    mPeakOneAnimation.stop();
                    mPeakTwoAnimation.stop();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            holderReference.get().mPeakOne.setImageResource(0);
            holderReference.get().mPeakTwo.setImageResource(0);
        }
        return view;
    }
}
