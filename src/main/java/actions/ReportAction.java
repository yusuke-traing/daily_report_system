package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 * @author 01043429
 *
 */
public class ReportAction extends ActionBase {

    private ReportService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        //メソッドを実行
        invoke();
        service.close();

    }

    public void index() throws ServletException, IOException{

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
       long reportsCount = service.countAll();

       putRequestScope(AttributeConst.REPORTS, reports);
       putRequestScope(AttributeConst.REP_COUNT, reportsCount);
       putRequestScope(AttributeConst.PAGE, page);
       putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);

       //セッションにフラッシュメッセージが設定されている場合は、リクエストスコープに移し替え、セッションからは削除する
       String flush = getSessionScope(AttributeConst.FLUSH);
       if (flush != null) {
           putRequestScope(AttributeConst.FLUSH, flush);
           removeSessionScope(AttributeConst.FLUSH);
       }

       //一覧画面を表示
       forward(ForwardConst.FW_REP_INDEX);
    }

    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv);

        //新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);
    }

}