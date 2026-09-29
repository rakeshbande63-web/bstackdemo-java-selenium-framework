package listeners;

import org.testng.IExecutionListener;
import reporting.ExtentManager;

/** Flushes the shared Extent report once, after the whole suite (API + UI) finishes. */
public class SuiteReportListener implements IExecutionListener {
    @Override
    public void onExecutionFinish() {
        ExtentManager.flush();
    }
}
