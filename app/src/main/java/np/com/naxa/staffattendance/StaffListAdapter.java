package np.com.naxa.staffattendance;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import np.com.naxa.staffattendance.pojo.Staff;
import np.com.naxa.staffattendance.utlils.FlipAnimator;

public class StaffListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Staff> filetredsitelist;
    private List<Staff> staffList;
    private OnStaffItemClickListener listener;
    private SparseBooleanArray selectedItems;
    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;


    StaffListAdapter(Context mContext, List<Staff> staffList, OnStaffItemClickListener listener) {
        this.mContext = mContext;
        this.staffList = staffList;
        this.filetredsitelist = staffList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_list_row, parent, false);

        return new StaffVH(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        final StaffVH staffVH = (StaffVH) holder;
        staffVH.staffName.setText(staff.getName());
        staffVH.staffType.setText(staff.getStaffType());
        staffVH.iconText.setVisibility(View.VISIBLE);
        applyIconAnimation(staffVH, holder.getAdapterPosition());
        staffVH.imgProfile.setImageResource(R.drawable.circle_blue);
        staffVH.iconText.setText(staff.getName().substring(0,1));

        staffVH.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStaffClick(holder.getAdapterPosition());
            }
        });

       // staffVH.rootLayout.setActivated(false);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public void toggleSelection(int pos) {

        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    private void applyIconAnimation(StaffVH holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {

            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }


    public class StaffVH extends RecyclerView.ViewHolder {
        private TextView staffName, siteAddress, sitePhone, staffType, sitePendingFormsNumber, site, iconText, timestamp, tvTagOfflineSite;
        private ImageView iconImp, imgProfile;
        private RelativeLayout iconContainer, iconBack, iconFront;
        private RelativeLayout rootLayout;
        private CardView card;

        public StaffVH(View view) {
            super(view);
            card = view.findViewById(R.id.card_site_lst_row);
            iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            iconContainer = view.findViewById(R.id.icon_container);
            rootLayout = view.findViewById(R.id.root_layout);

            staffName = view.findViewById(R.id.staff_list_row_name);
            siteAddress = view.findViewById(R.id.staff_list_row_email);
            sitePhone = view.findViewById(R.id.staff_list_row_phone);
            staffType = view.findViewById(R.id.staff_list_row_type);
            iconText = view.findViewById(R.id.icon_text);
            tvTagOfflineSite = view.findViewById(R.id.staff_list_row_status);
            imgProfile = view.findViewById(R.id.icon_profile);
        }
    }

    public interface OnStaffItemClickListener {
        void onStaffClick(int pos);

        void onStaffLongClick(int pos);
    }
}

