package com.tenneco.tennecoapp.Hourly;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.ProductionLineAdapter;
import com.tenneco.tennecoapp.Adapter.RealmProductionLineAdapter;
import com.tenneco.tennecoapp.Daily.DailyActivity;
import com.tenneco.tennecoapp.Graphics.GraphicActivity;
import com.tenneco.tennecoapp.Lines.ConfigLineActivity;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.Util;


public class HourlyFragment extends Fragment implements HourlyContract.View,ProductionLineAdapter.ItemInteraction, RealmProductionLineAdapter.ItemInteraction {
    private MainActivity main;
    private DatabaseReference dbPLines;
    private DatabaseReference dbTPLines;
    private DatabaseReference dbLine;
    private ProductionLineAdapter mAdapter;
    private RealmProductionLineAdapter mRAdapter;
    private ArrayList<Line> mLines;
    private HourlyContract.Presenter mPresenter;
    private String lineId;
    private Line mLine;
    private int admin=0;
    RealmResults<com.tenneco.tennecoapp.Model.realm.Line> mRLines;
    private Query postsQuery;
    private ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Line line = dataSnapshot.getValue(Line.class);
            if (line!=null) {
                dbTPLines =  FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(getContext())).child(line.getParentId());
                dbTPLines.child(line.getId()).setValue(line);
                mLines = new ArrayList<>();
                mLines.add(line);
                //mAdapter.setLines(mLines);
                //mAdapter.notifyDataSetChanged();
                hideProgressBar();
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Line> lines = new ArrayList<>();
            ArrayList<Line> dlines = new ArrayList<>();
            boolean today = false, tomorrow = false;
            String date = Utils.getDateString();
            String t0morrow = Utils.getTomorrowDateString();


            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
            {
                Line line = itemSnapshot.getValue(Line.class);

                if (line!=null) {
                    if (Utils.getMonth(line.getDate()).equals(Utils.getMonth(Utils.getDateString())))
                        lines.add(line);
                    else
                        dlines.add(line);

                    if (line.getDate()!=null&& line.getDate().equals(date))
                        today=true;

                    if (line.getDate()!=null&& line.getDate().equals(t0morrow))
                        tomorrow=true;

                }
            }

            if (lines.size()>0) {

                try (Realm realm = Realm.getDefaultInstance()) {
                        realm.beginTransaction();

                        mRLines.deleteAllFromRealm();

                        for (Line line : lines) {
                            com.tenneco.tennecoapp.Model.realm.Line l = new com.tenneco.tennecoapp.Model.realm.Line(
                                    line.getId(), line.getName(), line.getCode(), line.getDate(), line.getProducts().get(0).getFirst().getCumulativePlanned(),
                                    line.getProducts().get(0).getSecond().getCumulativePlanned(), line.getProducts().get(0).getThird().getCumulativePlanned(),
                                    line.getFirst().getCumulativeActual(),
                                    line.getSecond().getCumulativeActual(),
                                    line.getThird().getCumulativeActual(),
                                    true, line.isSchedule(), StorageUtils.getPlantId(main));
                            l.setParentId(line.getParentId());
                            realm.copyToRealmOrUpdate(l);
                        }

                        realm.commitTransaction();

                    } catch (Exception ignored) {

                    } finally {
                        getRealmResults();
                    }
                }


            mLines = new ArrayList<>();
            for (int size = lines.size()-1; size>=0 ; size--)
                mLines.add(lines.get(size));

            if(!today)
                showFb();
            else
                hideFb();

            if(!tomorrow && Utils.compareTime(Utils.getTimeString(),"10:00:00 PM"))
                showTw();
            else
                hideTw();

            //mAdapter.setLines(mLines);
            //mAdapter.notifyDataSetChanged();
            hideProgressBar();

/*            for (Line line : dlines) {
                if (getContext() != null) {
                    dbTPLines = FirebaseDatabase.getInstance().getReference(Line.AVAILABLE_DATES).child(StorageUtils.getPlantId(getContext())).child(Utils.getYear(line.getDate()))
                            .child(Utils.getMonth(line.getDate())).child(Utils.getDay(line.getDate()));
                    dbTPLines.child(line.getId()).setValue(line.getId());
                    if (!Utils.getMonth(line.getDate()).equals(Utils.getMonth(Utils.getDateString()))) {
                        //dbPLines.removeEventListener(valueEventListener);
                        dbPLines.child(line.getId()).removeValue();
                    }
                }
            }*/


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            hideProgressBar();
        }
    };
    @BindView(R.id.rv_lines) RecyclerView mRvLines;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;
    @BindView(R.id.fb_tomorrow) FloatingActionButton mFbTAdd;
    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.tv_shift1) TextView mTvFirstShift;
    @BindView(R.id.tv_shift2) TextView mTvSecondShift;
    @BindView(R.id.tv_shift3) TextView mTvThirdShift;
    @BindView(R.id.line)CardView mCvLine;
    @BindView(R.id.ll_shift) LinearLayout mLlShifts;


    @OnClick(R.id.fb_add) void onAdd(){
        addNewLine();
        hideFb();
    }

    @OnClick(R.id.fb_tomorrow) void onAddTomorrow(){addTNewLine();
    hideTw();
    }

    @OnClick(R.id.tv_name) void onTouch(){
        Intent intent = new Intent(getActivity(), GraphicActivity.class);
        intent.putExtra("lineId",lineId);
        startActivity(intent);

       /* if (mLlShifts.getVisibility()==View.VISIBLE)
            hideShifts();
        else
            showShifts();
            */
    }


    public HourlyFragment() {
        // Required empty public constructor
    }

    public static HourlyFragment newInstance(String lineId,int admin) {

        Bundle args = new Bundle();

        HourlyFragment fragment = new HourlyFragment();
        args.putString("id",lineId);
        args.putInt("admin",admin);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hourly, container, false);
        ButterKnife.bind(this,view);
        if (getArguments()!=null) {
            lineId = getArguments().getString("id");
            admin  =getArguments().getInt("admin");
        }
        dbPLines = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(getContext())).child(lineId);
        dbLine =  FirebaseDatabase.getInstance().getReference(Line.DB_LINE).child(StorageUtils.getPlantId(getContext()));
        //dbPLines = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(getContext()));
        //dbPLines = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(Line.DB_PRODUCTION_LINE);
        // dbTLine =  FirebaseDatabase.getInstance().getReference(Line.DB_LINE).child(StorageUtils.getPlantId(getContext()));

        mLines = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRAdapter = new RealmProductionLineAdapter(null,this);
        getRealmResults();
        mRvLines.setAdapter(mRAdapter);
        return view;
    }

    @Override
    public void onItemClick(String lineId) {
        launchDaily(lineId);
    }

    @Override
    public void onLongClick(String lineId) {
        if (admin==3) {
            Intent intent = new Intent(main, ConfigLineActivity.class);
            intent.putExtra("cell", true);
            intent.putExtra("id", lineId);
            intent.putExtra("parentId",mLine.getId());
            startActivity(intent);
        }

    }

    @Override
    public void getLines() {
 /*

postsQuery = dbPLines.child("-La-XOCsi3i2KHRys0oa");

*/
        if (admin>1)
            postsQuery = dbPLines.limitToLast(10);
        else
            postsQuery = dbPLines.limitToLast(2);


        postsQuery.addValueEventListener(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();//

    }

    @Override
    public void getLine() {
        Query postsQuery;
        postsQuery = dbLine.child(lineId);
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLine = dataSnapshot.getValue(Line.class);
                if (mLine!=null) {
                    setLine();
                    getRealmResults();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments()!=null) {
            lineId = getArguments().getString("id");
            admin  =getArguments().getInt("admin");
        }
        main = (MainActivity) getActivity();
        if (main != null) {
            main.hideMenu();
        }
        getLine();
    }

    @Override
    public void addNewLine() {

        Line line = new Line(mLine);
        String id = dbPLines.push().getKey();
        line.setId(id+mLine.getCode()+Utils.getDateStamp());
        line.setCode(mLine.getCode());
        line.setDescription(mLine.getDescription());
        line.setDate(Utils.getDateString());
        line.setParentId(lineId);
        line.setPassword(mLine.getPassword());
        line.setProducts(mLine.getProducts());
        dbPLines.child(line.getId()).setValue(line);
        dbTPLines = FirebaseDatabase.getInstance().getReference(Line.AVAILABLE_DATES).child(StorageUtils.getPlantId(getContext())).child(Utils.getYear(line.getDate())).child(Utils.getMonth(line.getDate())).child(Utils.getDay(line.getDate()));
        dbTPLines.child(line.getId()).setValue(line.getId());
    }

    @Override
    public void addTNewLine() {
        Line line = new Line(mLine);
        line.setId(lineId+line.getCode()+Utils.getTomorrowDateString().replaceAll("/",""));
        line.setCode(mLine.getCode());
        line.setDescription(mLine.getDescription());
        line.setDate(Utils.getTomorrowDateString());
        line.setParentId(lineId);
        line.setPassword(mLine.getPassword());
        line.setProducts(mLine.getProducts());
        dbPLines.child(line.getId()).setValue(line);
        dbTPLines = FirebaseDatabase.getInstance().getReference(Line.AVAILABLE_DATES).child(StorageUtils.getPlantId(getContext())).child(Utils.getYear(line.getDate())).child(Utils.getMonth(line.getDate())).child(Utils.getDay(line.getDate()));
        dbTPLines.child(line.getId()).setValue(line.getId());
    }

    @Override
    public void hideProgressBar() {
        if (getContext()!=null) {
            mPbLoading.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showFb() {
        if (getContext()!=null)
            mFbAdd.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void hideFb() {
        if (getContext()!=null)
            mFbAdd.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showTw() {
        if (getContext()!=null)
            mFbTAdd.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void hideTw() {
        if (getContext()!=null)
            mFbTAdd.setVisibility(View.GONE);
    }

    @Override
    public void setLine() {
        String text = mLine.getName() + " Analytics";
        mTvName.setText(text);
        mTvFirstShift.setText(mLine.getFirst().getCumulativePlanned());
        mTvSecondShift.setText(mLine.getSecond().getCumulativePlanned());
        mTvThirdShift.setText(mLine.getThird().getCumulativePlanned());
        mCvLine.setVisibility(View.VISIBLE);
        getLines();
    }

    @Override
    public void hideShifts() {
        mLlShifts.setVisibility(View.GONE);
    }

    @Override
    public void showShifts() {
        mLlShifts.setVisibility(View.VISIBLE);
    }

    @Override
    public void launchDaily(String lineId) {
        Intent intent = new Intent(main, DailyActivity.class);
        intent.putExtra("id",lineId);
        if (mLine!=null &&  mLine.getId()!=null)
        intent.putExtra("parentId",mLine.getId());
        else
            intent.putExtra("parentId",this.lineId);
        startActivity(intent);
    }

    @Override
    public void getRealmResults() {
        Realm realm = Realm.getDefaultInstance();
        String plantId="";
        if(main!=null)
            plantId=StorageUtils.getPlantId(main);
        else
        if (getActivity()!=null)
            plantId = StorageUtils.getPlantId(getActivity());

         mRLines = realm.where(com.tenneco.tennecoapp.Model.realm.Line.class)
                .equalTo("isPline",true).equalTo("plantId",plantId)
                .equalTo("parentId",lineId).sort("date", Sort.DESCENDING).findAll();


        if (mRLines!=null && mRLines.size()>0)
        {
            if (mRLines.get(0)!=null && mRLines.get(0).getName()!=null) {
                String text = mRLines.get(0).getName() + " Analytics";
                mTvName.setText(text);
                mCvLine.setVisibility(View.VISIBLE);
            }
        }
        mRAdapter.setLines(mRLines);
        mRAdapter.notifyDataSetChanged();
    }

    @Override
    public void bindPresenter(HourlyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
