package com.tenneco.tennecoapp.Templates;

import android.content.Context;

import com.tenneco.tennecoapp.Model.Template;
import com.tenneco.tennecoapp.Model.Templates;
import com.tenneco.tennecoapp.R;

/**
 * Created by ghoss on 19/12/2018.
 */
public class TemplatePresenter implements TemplateContract.Presenter {
    private TemplateContract.View mView;
    private Context context;

    TemplatePresenter(TemplateContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public Templates setData(Templates templates) {
        Templates mTemplates = new Templates();
        Template downtimeStart,downtimeEnd,ftqs,reject1,reject2,reject3,report;

        if (templates==null)
            templates = new Templates();

        if (templates.getDowntimeStart()==null)
        downtimeStart = new Template(context.getString(R.string.downtime_subject), context.getString(R.string.this_is_a_notification_that), context.getString(R.string.please_take_inmidiate_action));
        else
            downtimeStart = templates.getDowntimeStart();

        if(templates.getDowntimeEnd()==null)
            downtimeEnd = new Template(context.getString(R.string.is_back_to_production), context.getString(R.string.this_is_a_notification_that), context.getString(R.string.thank_you));
        else
            downtimeEnd = templates.getDowntimeEnd();

        if(templates.getFtqs()==null)
            ftqs = new Template(context.getString(R.string.ftq_over_10_escalation_and_actions),
                context.getString(R.string.please_see_below_the_actions_taken_to_solve_ftq_leak_issues_faced_on_current_shift_on)
                , context.getString(R.string.thank_you));
        else
            ftqs = templates.getFtqs();

        if (templates.getReject1() == null)
            reject1 = new Template(context.getString(R.string.produced_a_first_rejected_piece_due_to), context.getString(R.string.this_is_a_notification_that), context.getString(R.string.first_occurrence));
        else
            reject1 = templates.getReject1();

        if(templates.getReject2()==null)
            reject2 = new Template(context.getString(R.string.produced_a_second_rejected_piece_due_to), context.getString(R.string.this_is_a_notification_that),
                context.getString(R.string.second_occurrence_next_notification_will_be_send_to_group_leaders));
        else
            reject2 = templates.getReject2();

        if (templates.getReject3()==null)
            reject3 = new Template(context.getString(R.string.produced_rejected_piece), context.getString(R.string.this_is_a_notification_that),
                context.getString(R.string.next_notification_will_be_send_to_group_leaders));
        else
            reject3 = templates.getReject3();

        if (templates.getReport()==null)
            report= new Template(context.getString(R.string.daily_report), context.getString(R.string.this_is_the_report_of_all_the_lines_of_today),
                context.getString(R.string.thank_you));
        else
            report = templates.getReport();


        templates.setDowntimeStart(downtimeStart);
        templates.setDowntimeEnd(downtimeEnd);
        templates.setFtqs(ftqs);
        templates.setReject1(reject1);
        templates.setReject2(reject2);
        templates.setReject3(reject3);
        templates.setReport(report);



        return templates;
    }


    @Override
    public void bindView(TemplateContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }


}
