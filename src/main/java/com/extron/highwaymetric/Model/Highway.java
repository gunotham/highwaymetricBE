package com.extron.highwaymetric.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.MultiLineString;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "highway")
@Getter
@Setter
public class Highway {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String ref;
    private String description;
    
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(name="geom")
    private MultiLineString geom;//test code

    @Enumerated(EnumType.STRING)
    private HighwayStatus status = HighwayStatus.PLANNING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    @OneToMany(mappedBy = "highway", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     private List<NewsArticle> news;
    
    @Column(name = "created_at", nullable = false, updatable = false)
     private LocalDateTime createdAt;
     
     @Column(name = "updated_at")
     private LocalDateTime updatedAt;
     
     @Column(name = "estimated_budget")
     private Double estimatedBudget;
     
     @Column(name = "actual_cost")
     private Double actualCost;
     
     @Column(name = "rework_count")
     private Integer reworkCount = 0;
     
     @Column(name = "completion_date")
     private LocalDateTime completionDate;
     
     @Column(name = "length_km")
     private Double lengthKm;
     
     @Column(name = "state")
     private String state;
     
     @PrePersist
     protected void onCreate() {
         createdAt = LocalDateTime.now();
         updatedAt = LocalDateTime.now();
     }
       
     @PreUpdate
     protected void onUpdate() {
         updatedAt = LocalDateTime.now();
     }
    
}
