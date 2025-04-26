package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.Report;
import com.bblets.baibuy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByReportedUser(User user);
}
