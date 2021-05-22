package com.example.samsunghackathon2021.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.samsunghackathon2021.R;

import java.util.ArrayList;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.viewHolder> {

    Context context;
    Activity activity;
    ArrayList<NoteModel> arrayList;
    DatabaseHelper database_helper;
    ImageView image;

    public NotesAdapter(Context context,Activity activity, ArrayList<NoteModel> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
    }

    @Override
    public NotesAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.viewHolder holder, final int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.mode.setText(arrayList.get(position).getDes());
        int[] images = {R.drawable.vegetables1,R.drawable.vegetables2,R.drawable.vegetables3,R.drawable.vegetables4};
        Random rand = new Random();
        image.setImageResource(images[rand.nextInt(images.length)]);
        //holder.description.setText(arrayList.get(position).getDes());
        database_helper = new DatabaseHelper(context);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, mode;
        ImageView delete, edit;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.name);
            mode = (TextView) itemView.findViewById(R.id.mode);
            image = (ImageView) itemView.findViewById(R.id.flag);

            //description = (TextView) itemView.findViewById(R.id.description);
            //delete = (ImageView) itemView.findViewById(R.id.delete);
            //edit = (ImageView) itemView.findViewById(R.id.edit);
        }
    }

    /*public void showDialog(final int pos) {
        final EditText title, des;
        Button submit;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        submit = (Button) dialog.findViewById(R.id.submit);

        title.setText(arrayList.get(pos).getTitle());
        des.setText(arrayList.get(pos).getDes());

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Title");
                }else if(des.getText().toString().isEmpty()) {
                    des.setError("Please Enter Description");
                }else {
                    //updating note
                    database_helper.updateNote(title.getText().toString(), des.getText().toString(), arrayList.get(pos).getID());
                    arrayList.get(pos).setTitle(title.getText().toString());
                    arrayList.get(pos).setDes(des.getText().toString());
                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }*/
}