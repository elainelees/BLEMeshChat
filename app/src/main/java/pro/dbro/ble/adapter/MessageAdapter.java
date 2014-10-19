package pro.dbro.ble.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pro.dbro.ble.ChatApp;
import pro.dbro.ble.R;
import pro.dbro.ble.model.Message;
import pro.dbro.ble.model.MessageTable;
import pro.dbro.ble.model.Peer;

/**
 * Created by davidbrodsky on 10/19/14.
 */
public class MessageAdapter extends RecyclerViewCursorAdapter<MessageAdapter.ViewHolder> {
    public static final String TAG = "MessageAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView senderView;
        public TextView messageView;

        public ViewHolder(View v) {
            super(v);
            senderView = (TextView) v.findViewById(R.id.sender);
            messageView = (TextView) v.findViewById(R.id.messageBody);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        // TODO: cache message sender alias to avoid additional query
        Cursor peer = ChatApp.getPeerById(mContext, cursor.getInt(cursor.getColumnIndex(MessageTable.peerId)));
        if (peer != null && peer.moveToFirst()) {
            holder.senderView.setText(new Peer(peer).getAlias());
        } else {
            holder.senderView.setText("?");
        }
        holder.messageView.setText(cursor.getString(cursor.getColumnIndex(MessageTable.body)));
    }

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public MessageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    protected void onContentChanged() {
        Log.i(TAG, "onContentChanged");
        changeCursor(ChatApp.getMessagesToSend(mContext));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return new ViewHolder(v);
    }
}
