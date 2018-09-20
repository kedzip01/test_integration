package app.domain;

import java.time.OffsetDateTime;

public class RecentDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private Long id;
    @Column(name="USER_ID")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "RECENT_DEVICE_ID", referencedColumnName = "DEVICE_ID")
    private Device recentDevice;

    @Column(name = "RECENT_DATE")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private OffsetDateTime recentDate;

    @Column(name="FAVOURITE_DATE")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private OffsetDateTime favouriteDate;
}
