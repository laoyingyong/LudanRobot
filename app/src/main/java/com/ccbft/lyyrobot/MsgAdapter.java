package com.ccbft.lyyrobot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccbft.lyyrobot.domain.Msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> msgList;
    private long lastTime=0;
    private long nowTime;
    private Context context;
    private int timeColor;
    private int nameColor;


    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    public MsgAdapter(List<Msg> msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
    }

    public MsgAdapter(List<Msg> msgList, Context context, int timeColor) {
        this.msgList = msgList;
        this.context = context;
        this.timeColor = timeColor;
    }

    public MsgAdapter(List<Msg> msgList, Context context, int timeColor, int nameColor) {
        this.msgList = msgList;
        this.context = context;
        this.timeColor = timeColor;
        this.nameColor = nameColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.rightProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ProfileActivity.class));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rightTime.setTextColor(timeColor);
        holder.leftTime.setTextColor(timeColor);
        holder.leftName.setTextColor(nameColor);
        holder.rightName.setTextColor(nameColor);
        Msg msg = msgList.get(position);
        nowTime=System.currentTimeMillis();
        if(msg.getType()==Msg.TYPE_RECEIVED){//如果是接收到的消息
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            if(nowTime-lastTime>5000){//如果两次时间超过了5秒钟就显示时间
                lastTime=nowTime;
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simpleDateFormat.format(new Date());
                holder.leftTime.setText(format);
            }
            else {//否则就隐藏时间
                holder.leftTime.setVisibility(View.GONE);
            }
        }
        else if(msg.getType()==Msg.TYPE_SENT){//如果是发送的消息
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
            if(nowTime-lastTime>5000){
                lastTime=nowTime;
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simpleDateFormat.format(new Date());
                holder.rightTime.setText(format);
            }
            else {
                holder.rightTime.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View msgView;
        ImageView rightProfilePicture;
        ImageView leftProfilePicture;
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView leftTime;
        TextView rightTime;
        TextView leftName;
        TextView rightName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msgView=itemView;
            leftLayout=itemView.findViewById(R.id.left_layout);
            rightLayout=itemView.findViewById(R.id.right_layout);
            leftMsg=itemView.findViewById(R.id.left_tv);
            rightMsg=itemView.findViewById(R.id.right_tv);
            leftTime=itemView.findViewById(R.id.left_time);
            rightTime=itemView.findViewById(R.id.right_time);
            rightProfilePicture=itemView.findViewById(R.id.rightImage);
            leftProfilePicture=itemView.findViewById(R.id.leftImage);
            leftName=itemView.findViewById(R.id.leftName);
            rightName=itemView.findViewById(R.id.rightName);
        }
    }
}
