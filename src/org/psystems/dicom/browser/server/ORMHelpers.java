package org.psystems.dicom.browser.server;

import java.util.ArrayList;

import org.psystems.dicom.browser.client.proxy.DiagnosisProxy;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.EmployeeProxy;
import org.psystems.dicom.browser.client.proxy.ManufacturerDeviceProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.QueryDirectionProxy;
import org.psystems.dicom.browser.client.proxy.ServiceProxy;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.commons.orm.entity.Diagnosis;
import org.psystems.dicom.commons.orm.entity.Direction;
import org.psystems.dicom.commons.orm.entity.Employee;
import org.psystems.dicom.commons.orm.entity.ManufacturerDevice;
import org.psystems.dicom.commons.orm.entity.Patient;
import org.psystems.dicom.commons.orm.entity.QueryDirection;
import org.psystems.dicom.commons.orm.entity.Service;
import org.psystems.dicom.commons.orm.entity.Study;

/**
 * @author dima_d
 * 
 *         Хелперы для ORM
 * 
 */
/**
 * @author dima_d
 * 
 */
public class ORMHelpers {

    /**
     * @param study
     * @return
     */
    public static StudyProxy getStudyProxy(Study study) {

	if (study == null)
	    return null;

	StudyProxy proxy = new StudyProxy();
	proxy.setId(study.getId());
	proxy.setStudyModality(study.getStudyModality());
	proxy.setStudyInstanceUID(study.getStudyInstanceUID());
	proxy.setManufacturerModelName(study.getManufacturerModelName());
	proxy.setPatientName(study.getPatientName());
	proxy.setPatientSex(study.getPatientSex());
	proxy.setPatientId(study.getPatientId());
	proxy.setPatientBirthDate(study.getPatientBirthDate());
	proxy.setStudyId(study.getStudyId());
	proxy.setStudyType(study.getStudyType());
	proxy.setStudyDate(study.getStudyDate());
	proxy.setStudyViewprotocolDate(study.getStudyViewprotocolDate());
	proxy.setStudyDoctor(study.getStudyDoctor());
	proxy.setStudyOperator(study.getStudyOperator());
	proxy.setStudyDescription(study.getStudyDescription());
	proxy.setStudyViewprotocol(study.getStudyViewprotocol());
	proxy.setStudyResult(study.getStudyResult());
	proxy.setStudyDateTimeModify(study.getStudyDateTimeModify());
	proxy.setStudyDateTimeRemoved(study.getStudyDateTimeRemoved());

	if (study.getDirection() != null) {
	    proxy.setDirection(getDirectionProxy(study.getDirection()));
	}
	return proxy;
    }

    /**
     * TODO Пока не требуется.
     * 
     * @param proxy
     * @return
     */
    public static Study getStudy(StudyProxy proxy) {
	return null;

    }

    /**
     * @param q
     * @return
     */
    public static QueryDirection getQuerydirection(QueryDirectionProxy q) {

	if (q == null)
	    return null;

	QueryDirection qdrn = new QueryDirection();
	qdrn.setDateDirection(q.getDateDirection());
	qdrn.setDateTimePlannedBegin(q.getDateTimePlannedBegin());
	qdrn.setDateTimePlannedEnd(q.getDateTimePlannedEnd());
	qdrn.setDirectionId(q.getDirectionId());
	qdrn.setDirectionLocation(q.getDirectionId());
	qdrn.setDoctorDirectCode(q.getDoctorDirectCode());
	qdrn.setDoctorDirectName(q.getDoctorDirectName());
	qdrn.setDoctorPerformedCode(q.getDoctorPerformedCode());
	qdrn.setDoctorPerformedName(q.getDoctorPerformedName());
	qdrn.setId(q.getId());
	qdrn.setManufacturerDevice(q.getManufacturerDevice());
	qdrn.setPatientBirthDate(q.getPatientBirthDate());
	qdrn.setPatientId(q.getPatientId());
	qdrn.setPatientName(q.getPatientName());
	qdrn.setPatientSex(q.getPatientSex());
	return qdrn;
    }

    /**
     * @param p
     * @return
     */
    public static PatientProxy getPatientProxy(Patient p) {

	if (p == null)
	    return null;

	PatientProxy proxy = new PatientProxy();
	proxy.setId(p.getId());
	proxy.setPatientBirthDate(p.getPatientBirthDate());
	proxy.setPatientId(p.getPatientId());
	proxy.setPatientName(p.getPatientName());
	proxy.setPatientSex(p.getPatientSex());
	proxy.setPatientShortName(p.getPatientShortName());
	return proxy;
    }

    /**
     * @param prpxy
     * @return
     */
    public static Patient getPatient(PatientProxy proxy) {

	if (proxy == null)
	    return null;
	Patient pat = new Patient();
	pat.setId(proxy.getId());
	pat.setPatientBirthDate(proxy.getPatientBirthDate());
	pat.setPatientId(proxy.getPatientId());
	pat.setPatientName(proxy.getPatientName());
	pat.setPatientSex(proxy.getPatientSex());
	return pat;

    }

    /**
     * @param e
     * @return
     */
    public static EmployeeProxy getEmployeeProxy(Employee e) {

	if (e == null)
	    return null;

	EmployeeProxy proxy = new EmployeeProxy();
	proxy.setEmployeeCode(e.getEmployeeCode());
	proxy.setEmployeeName(e.getEmployeeName());
	proxy.setEmployeeType(e.getEmployeeType());
	return proxy;
    }

    /**
     * @param proxy
     * @return
     */
    public static Employee getEmployee(EmployeeProxy proxy) {

	if (proxy == null)
	    return null;

	Employee emp = new Employee();
	emp.setEmployeeCode(proxy.getEmployeeCode());
	emp.setEmployeeName(proxy.getEmployeeName());
	emp.setEmployeeType(proxy.getEmployeeType());
	return emp;
    }

    /**
     * @param d
     * @return
     */
    public static DiagnosisProxy getDiagnosisProxy(Diagnosis d) {

	if (d == null)
	    return null;

	DiagnosisProxy proxy = new DiagnosisProxy();
	proxy.setDiagnosisCode(d.getDiagnosisCode());
	proxy.setDiagnosisDescription(d.getDiagnosisDescription());
	proxy.setDiagnosisSubType(d.getDiagnosisSubType());
	proxy.setDiagnosisType(d.getDiagnosisType());
	return proxy;
    }

    /**
     * @param proxy
     * @return
     */
    public static Diagnosis getDiagnosis(DiagnosisProxy proxy) {

	if (proxy == null)
	    return null;

	Diagnosis dia = new Diagnosis();
	dia.setDiagnosisCode(proxy.getDiagnosisCode());
	dia.setDiagnosisDescription(proxy.getDiagnosisDescription());
	dia.setDiagnosisSubType(proxy.getDiagnosisSubType());
	dia.setDiagnosisType(proxy.getDiagnosisType());
	return dia;
    }

    /**
     * @param d
     * @return
     */
    public static ManufacturerDeviceProxy getManufacturerDeviceProxy(ManufacturerDevice d) {

	if (d == null)
	    return null;

	ManufacturerDeviceProxy proxy = new ManufacturerDeviceProxy();
	proxy.setManufacturerModelDescription(d.getManufacturerModelDescription());
	proxy.setManufacturerModelName(d.getManufacturerModelName());
	proxy.setModality(d.getModality());
	proxy.setManufacturerModelTypeDescription(d.getManufacturerModelTypeDescription());
	return proxy;
    }

    public static ManufacturerDevice getManufacturerDevice(ManufacturerDeviceProxy proxy) {

	if (proxy == null)
	    return null;

	ManufacturerDevice dev = new ManufacturerDevice();
	dev.setManufacturerModelDescription(proxy.getManufacturerModelDescription());
	dev.setManufacturerModelName(proxy.getManufacturerModelName());
	dev.setManufacturerModelTypeDescription(proxy.getManufacturerModelTypeDescription());
	dev.setModality(proxy.getModality());

	return dev;
    }

    /**
     * @param s
     * @return
     */
    public static ServiceProxy getServiceProxy(Service s) {

	if (s == null)
	    return null;

	ServiceProxy proxy = new ServiceProxy();
	proxy.setServiceAlias(s.getServiceAlias());
	proxy.setServiceCode(s.getServiceCode());
	proxy.setServiceDescription(s.getServiceDescription());
	proxy.setServiceCount(s.getServiceCount());
	return proxy;
    }

    /**
     * @param proxy
     * @return
     */
    public static Service getService(ServiceProxy proxy) {

	if (proxy == null)
	    return null;

	Service srv = new Service();
	srv.setServiceAlias(proxy.getServiceAlias());
	srv.setServiceCode(proxy.getServiceCode());
	srv.setServiceCount(proxy.getServiceCount());
	srv.setServiceDescription(proxy.getServiceDescription());

	return srv;
    }

    /**
     * @param d
     * @return
     */
    public static DirectionProxy getDirectionProxy(Direction d) {

	if (d == null)
	    return null;

	DirectionProxy proxy = new DirectionProxy();
	proxy.setDateDirection(d.getDateDirection());
	proxy.setDatePerformed(d.getDatePerformed());
	proxy.setDateTimeModified(d.getDateTimeModified());
	proxy.setDateTimePlanned(d.getDateTimePlanned());
	proxy.setDateTimeRemoved(d.getDateTimeRemoved());
	proxy.setDevice(getManufacturerDeviceProxy(d.getDevice()));

	if (d.getDiagnosisDirect() != null) {
	    ArrayList<DiagnosisProxy> diagnosis = new ArrayList<DiagnosisProxy>();
	    for (Diagnosis dia : d.getDiagnosisDirect()) {
		diagnosis.add(getDiagnosisProxy(dia));
	    }
	    proxy.setDiagnosisDirect(diagnosis.toArray(new DiagnosisProxy[diagnosis.size()]));
	}

	if (d.getDiagnosisPerformed() != null) {
	    ArrayList<DiagnosisProxy> diagnosis = new ArrayList<DiagnosisProxy>();
	    for (Diagnosis dia : d.getDiagnosisPerformed()) {
		diagnosis.add(getDiagnosisProxy(dia));
	    }
	    proxy.setDiagnosisPerformed(diagnosis.toArray(new DiagnosisProxy[diagnosis.size()]));
	}

	proxy.setDirectionCode(d.getDirectionCode());
	proxy.setDirectionId(d.getDirectionId());
	proxy.setDirectionLocation(d.getDirectionLocation());
	proxy.setDoctorDirect(getEmployeeProxy(d.getDoctorDirect()));
	proxy.setDoctorPerformed(getEmployeeProxy(d.getDoctorPerformed()));
	proxy.setId(d.getId());
	proxy.setPatient(getPatientProxy(d.getPatient()));

	if (d.getServicesDirect() != null) {
	    ArrayList<ServiceProxy> srvs = new ArrayList<ServiceProxy>();
	    for (Service srv : d.getServicesDirect()) {
		srvs.add(getServiceProxy(srv));
	    }
	    proxy.setServicesDirect(srvs.toArray(new ServiceProxy[srvs.size()]));
	}

	if (d.getServicesPerformed() != null) {
	    ArrayList<ServiceProxy> srvs = new ArrayList<ServiceProxy>();
	    for (Service srv : d.getServicesPerformed()) {
		srvs.add(getServiceProxy(srv));
	    }
	    proxy.setServicesPerformed(srvs.toArray(new ServiceProxy[srvs.size()]));
	}

	return proxy;
    }

    /**
     * @param proxy
     * @return
     */
    public static Direction getDirection(DirectionProxy proxy) {
	if (proxy == null)
	    return null;

	Direction drn = new Direction();
	drn.setDateDirection(proxy.getDateDirection());
	drn.setDatePerformed(proxy.getDatePerformed());
	drn.setDateTimeModified(proxy.getDateTimeModified());
	drn.setDateTimePlanned(proxy.getDateTimePlanned());
	drn.setDevice(getManufacturerDevice(proxy.getDevice()));

	ArrayList<Diagnosis> dias;

	dias = new ArrayList<Diagnosis>();
	for (DiagnosisProxy diaproxy : proxy.getDiagnosisDirect()) {
	    dias.add(getDiagnosis(diaproxy));
	}

	drn.setDiagnosisDirect(dias.toArray(new Diagnosis[dias.size()]));

	dias = new ArrayList<Diagnosis>();
	for (DiagnosisProxy diaproxy : proxy.getDiagnosisPerformed()) {
	    dias.add(getDiagnosis(diaproxy));
	}

	drn.setDiagnosisPerformed(dias.toArray(new Diagnosis[dias.size()]));

	drn.setDirectionCode(proxy.getDirectionCode());
	drn.setDirectionId(proxy.getDirectionId());
	drn.setDirectionLocation(proxy.getDirectionLocation());
	drn.setDoctorDirect(getEmployee(proxy.getDoctorDirect()));
	drn.setDoctorPerformed(getEmployee(proxy.getDoctorPerformed()));
	drn.setId(proxy.getId());
	drn.setPatient(getPatient(proxy.getPatient()));

	ArrayList<Service> srvs;

	srvs = new ArrayList<Service>();
	for (ServiceProxy srvproxy : proxy.getServicesDirect()) {
	    srvs.add(getService(srvproxy));
	}

	drn.setServicesDirect(srvs.toArray(new Service[srvs.size()]));

	srvs = new ArrayList<Service>();
	for (ServiceProxy srvproxy : proxy.getServicesPerformed()) {
	    srvs.add(getService(srvproxy));
	}

	drn.setServicesPerformed(srvs.toArray(new Service[srvs.size()]));

	return drn;

    }

}
