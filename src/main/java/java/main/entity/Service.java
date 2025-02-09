package java.main.entity;

import java.main.enums.ServiceState;
import java.main.enums.ServiceType;

public class Service extends Entity {
    private Driver assignedDriver;
    private ServiceType serviceType;
    private ServiceState serviceState;

    public Service(Long id, Driver assignedDriver, ServiceType serviceType, ServiceState serviceState) {
        super(id);
        this.assignedDriver = assignedDriver;
        this.serviceType = serviceType;
        this.serviceState = serviceState;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public ServiceState getServiceState() {
        return serviceState;
    }
}
