package com.example.teachergradingsheet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> studentList;
    private final OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onEditClick(Student student);
        void onDeleteClick(Student student);
    }

    public StudentAdapter(List<Student> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.listener    = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student s = studentList.get(position);
        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvName.setText(s.getName());
        holder.tvStudentId.setText(s.getStudentId());
        holder.tvTotal.setText(String.format("%.2f", s.getTotalScore()));
        holder.tvGrade.setText(s.getGrade());

        if (s.isPassed()) {
            holder.tvRemarks.setText("PASSED");
            holder.tvRemarks.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvRemarks.setText("FAILED");
            holder.tvRemarks.setBackgroundColor(Color.parseColor("#C62828"));
        }

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F8F9FF"));
        }

        holder.tvEdit.setOnClickListener(v -> listener.onEditClick(s));
        holder.tvDelete.setOnClickListener(v -> listener.onDeleteClick(s));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateList(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumber;
        TextView tvName;
        TextView tvStudentId;
        TextView tvTotal;
        TextView tvGrade;
        TextView tvRemarks;
        TextView tvEdit;
        TextView tvDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber    = itemView.findViewById(R.id.tvNumber);
            tvName      = itemView.findViewById(R.id.tvName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvTotal     = itemView.findViewById(R.id.tvTotal);
            tvGrade     = itemView.findViewById(R.id.tvGrade);
            tvRemarks   = itemView.findViewById(R.id.tvRemarks);
            tvEdit      = itemView.findViewById(R.id.tvEdit);
            tvDelete    = itemView.findViewById(R.id.tvDelete);
        }
    }
}