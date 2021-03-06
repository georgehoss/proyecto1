package com.tenneco.tennecoapp.Templates;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tenneco.tennecoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FTQTemplateFragment extends Fragment {
    private TemplatesActivity main;
    @BindView(R.id.et_subject)
    EditText mEtSubject;
    @BindView(R.id.et_body1) EditText mEtBody1;
    @BindView(R.id.et_body2) EditText mEtBody2;

    public FTQTemplateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ftqtemplate, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTemplate();
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (TemplatesActivity) getActivity();

        if (main!=null && main.mTemplates!=null)
        {
            mEtSubject.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    main.mTemplates.getFtqs().setSubject(editable.toString());
                }
            });

            mEtBody1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    main.mTemplates.getFtqs().setBody1(editable.toString());
                }
            });

            mEtBody2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    main.mTemplates.getFtqs().setBody2(editable.toString());
                }
            });

            updateTemplate();
        }
    }

    public void updateTemplate() {

        if (main!=null && main.mTemplates!=null) {
            mEtSubject.setText(main.mTemplates.getFtqs().getSubject());
            mEtBody1.setText(main.mTemplates.getFtqs().getBody1());
            mEtBody2.setText(main.mTemplates.getFtqs().getBody2());
        }
    }
}
