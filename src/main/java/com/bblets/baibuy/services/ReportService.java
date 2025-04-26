package com.bblets.baibuy.services;

import com.bblets.baibuy.models.Report;
import com.bblets.baibuy.models.Report.ReportStatus;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public void reportUser(User reporter, User reportedUser, String reason) {
        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(reason);
        reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public void markAsReviewed(Integer reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow();
        report.setStatus(ReportStatus.REVIEWED);
        reportRepository.save(report);
    }
}
