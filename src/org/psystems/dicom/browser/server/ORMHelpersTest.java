package org.psystems.dicom.browser.server;

import java.util.ArrayList;

import org.psystems.dicom.browser.client.proxy.DiagnosisProxy;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.EmployeeProxy;
import org.psystems.dicom.browser.client.proxy.ManufacturerDeviceProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.ServiceProxy;
import org.psystems.dicom.commons.orm.entity.Diagnosis;
import org.psystems.dicom.commons.orm.entity.Direction;
import org.psystems.dicom.commons.orm.entity.Employee;
import org.psystems.dicom.commons.orm.entity.ManufacturerDevice;
import org.psystems.dicom.commons.orm.entity.Patient;
import org.psystems.dicom.commons.orm.entity.Service;

import junit.framework.TestCase;

public class ORMHelpersTest extends TestCase {

    protected void setUp() throws Exception {
	super.setUp();
    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testGetStudyProxy() {
	fail("Not yet implemented");
    }

    public void testGetStudy() {
	fail("Not yet implemented");
    }

    public void testGetQuerydirection() {
	fail("Not yet implemented");
    }

    public void testGetPatientProxy() {

	Patient pat = new Patient();
	pat.setId(123);
	pat.setPatientBirthDate("1974-03-30");
	pat.setPatientId("ID123");
	pat.setPatientName("Иванов Иван Иванович");
	pat.setPatientSex("M");

	PatientProxy proxy = ORMHelpers.getPatientProxy(pat);
	assertEquals(pat.getId(), proxy.getId());
	assertEquals(pat.getPatientBirthDate(), proxy.getPatientBirthDate());
	assertEquals(pat.getPatientId(), proxy.getPatientId());
	assertEquals(pat.getPatientName(), proxy.getPatientName());
	assertEquals(pat.getPatientSex(), proxy.getPatientSex());
	assertEquals(pat.getPatientShortName(), proxy.getPatientShortName());
    }

    public void testGetPatient() {

	PatientProxy proxy = new PatientProxy();
	proxy.setId(123);
	proxy.setPatientBirthDate("1974-03-30");
	proxy.setPatientId("ID123");
	proxy.setPatientName("Иванов Иван Иванович");
	proxy.setPatientSex("M");
	proxy.setPatientShortName("ИВАИИ74");

	Patient pat = ORMHelpers.getPatient(proxy);
	assertEquals(pat.getId(), proxy.getId());
	assertEquals(pat.getPatientBirthDate(), proxy.getPatientBirthDate());
	assertEquals(pat.getPatientId(), proxy.getPatientId());
	assertEquals(pat.getPatientName(), proxy.getPatientName());
	assertEquals(pat.getPatientSex(), proxy.getPatientSex());
	assertEquals(pat.getPatientShortName(), proxy.getPatientShortName());

    }

    public void testGetEmployeeProxy() {

	Employee emp = new Employee();
	emp.setEmployeeCode("CODE1");
	emp.setEmployeeName("Сидоров Сидор Сидорович");
	emp.setEmployeeType(Employee.TYPE_DOCTOR);

	EmployeeProxy proxy = ORMHelpers.getEmployeeProxy(emp);
	assertEquals(emp.getEmployeeCode(), proxy.getEmployeeCode());
	assertEquals(emp.getEmployeeName(), proxy.getEmployeeName());
	assertEquals(emp.getEmployeeType(), proxy.getEmployeeType());
    }

    public void testGetEmployee() {

	EmployeeProxy proxy = new EmployeeProxy();
	proxy.setEmployeeCode("CODE1");
	proxy.setEmployeeName("Сидоров Сидор Сидорович");
	proxy.setEmployeeType(Employee.TYPE_DOCTOR);

	Employee emp = ORMHelpers.getEmployee(proxy);
	assertEquals(emp.getEmployeeCode(), proxy.getEmployeeCode());
	assertEquals(emp.getEmployeeName(), proxy.getEmployeeName());
	assertEquals(emp.getEmployeeType(), proxy.getEmployeeType());
    }

    public void testGetDiagnosisProxy() {

	Diagnosis dia = new Diagnosis();
	dia.setDiagnosisCode("Z01.11");
	dia.setDiagnosisDescription("DIAZ");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_MAIN);

	DiagnosisProxy proxy = ORMHelpers.getDiagnosisProxy(dia);
	assertEquals(dia.getDiagnosisCode(), proxy.getDiagnosisCode());
	assertEquals(dia.getDiagnosisDescription(), proxy.getDiagnosisDescription());
	assertEquals(dia.getDiagnosisSubType(), proxy.getDiagnosisSubType());
	assertEquals(dia.getDiagnosisType(), proxy.getDiagnosisType());
    }

    public void testGetDiagnosis() {

	DiagnosisProxy proxy = new DiagnosisProxy();
	proxy.setDiagnosisCode("Z01.11");
	proxy.setDiagnosisDescription("DIAZ");
	proxy.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	proxy.setDiagnosisType(Diagnosis.TYPE_MAIN);

	Diagnosis dia = ORMHelpers.getDiagnosis(proxy);
	assertEquals(dia.getDiagnosisCode(), proxy.getDiagnosisCode());
	assertEquals(dia.getDiagnosisDescription(), proxy.getDiagnosisDescription());
	assertEquals(dia.getDiagnosisSubType(), proxy.getDiagnosisSubType());
	assertEquals(dia.getDiagnosisType(), proxy.getDiagnosisType());

    }

    public void testGetManufacturerDeviceProxy() {

	ManufacturerDevice dev = new ManufacturerDevice();
	dev.setManufacturerModelDescription("РЕНЕКС ФЛЮОРОГРАФ");
	dev.setManufacturerModelName("РЕНЕКС");
	dev.setManufacturerModelTypeDescription("Для флюшки");
	dev.setModality("CR");

	ManufacturerDeviceProxy proxy = ORMHelpers.getManufacturerDeviceProxy(dev);
	assertEquals(dev.getManufacturerModelDescription(), proxy.getManufacturerModelDescription());
	assertEquals(dev.getManufacturerModelName(), proxy.getManufacturerModelName());
	assertEquals(dev.getManufacturerModelTypeDescription(), proxy.getManufacturerModelTypeDescription());
	assertEquals(dev.getModality(), proxy.getModality());
    }

    public void testGetManufacturerDevice() {

	ManufacturerDeviceProxy proxy = new ManufacturerDeviceProxy();
	proxy.setManufacturerModelDescription("РЕНЕКС ФЛЮОРОГРАФ");
	proxy.setManufacturerModelName("РЕНЕКС");
	proxy.setManufacturerModelTypeDescription("Для флюшки");
	proxy.setModality("CR");

	ManufacturerDevice dev = ORMHelpers.getManufacturerDevice(proxy);
	assertEquals(dev.getManufacturerModelDescription(), proxy.getManufacturerModelDescription());
	assertEquals(dev.getManufacturerModelName(), proxy.getManufacturerModelName());
	assertEquals(dev.getManufacturerModelTypeDescription(), proxy.getManufacturerModelTypeDescription());
	assertEquals(dev.getModality(), proxy.getModality());
    }

    public void testGetServiceProxy() {

	Service srv = new Service();
	srv.setServiceAlias("ALIAS");
	srv.setServiceCode("CODE");
	srv.setServiceCount(2);
	srv.setServiceDescription("DESCR");

	ServiceProxy proxy = ORMHelpers.getServiceProxy(srv);
	assertEquals(srv.getServiceAlias(), proxy.getServiceAlias());
	assertEquals(srv.getServiceCode(), proxy.getServiceCode());
	assertEquals(srv.getServiceCount(), proxy.getServiceCount());
	assertEquals(srv.getServiceDescription(), proxy.getServiceDescription());
    }

    public void testGetService() {

	ServiceProxy proxy = new ServiceProxy();
	proxy.setServiceAlias("ALIAS");
	proxy.setServiceCode("CODE");
	proxy.setServiceCount(2);
	proxy.setServiceDescription("DESCR");

	Service srv = ORMHelpers.getService(proxy);
	assertEquals(srv.getServiceAlias(), proxy.getServiceAlias());
	assertEquals(srv.getServiceCode(), proxy.getServiceCode());
	assertEquals(srv.getServiceCount(), proxy.getServiceCount());
	assertEquals(srv.getServiceDescription(), proxy.getServiceDescription());
    }

    public void testGetDirectionProxy() {

	Direction drn = new Direction();
	drn.setDateDirection("2011-06-24");
	drn.setDatePerformed("2011-06-25");
	drn.setDateTimeModified("2011-06-25 12:00:00");
	drn.setDateTimePlanned("2011-06-24 12:00:00");

	//
	ManufacturerDevice dev = new ManufacturerDevice();
	dev.setManufacturerModelDescription("РЕНЕКС ФЛЮОРОГРАФ");
	dev.setManufacturerModelName("РЕНЕКС");
	dev.setManufacturerModelTypeDescription("Для флюшки");
	dev.setModality("CR");

	drn.setDevice(dev);

	//
	ArrayList<Diagnosis> dias = new ArrayList<Diagnosis>();

	Diagnosis dia = new Diagnosis();
	dia.setDiagnosisCode("Z01.11");
	dia.setDiagnosisDescription("DIAZ11");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
	dias.add(dia);

	dia = new Diagnosis();
	dia.setDiagnosisCode("Z01.12");
	dia.setDiagnosisDescription("DIAZ12");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);
	dias.add(dia);

	drn.setDiagnosisDirect(dias.toArray(new Diagnosis[dias.size()]));

	dias = new ArrayList<Diagnosis>();

	dia = new Diagnosis();
	dia.setDiagnosisCode("Z01.13");
	dia.setDiagnosisDescription("DIAZ13");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
	dias.add(dia);

	dia = new Diagnosis();
	dia.setDiagnosisCode("Z01.14");
	dia.setDiagnosisDescription("DIAZ14");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);
	dias.add(dia);

	drn.setDiagnosisPerformed(dias.toArray(new Diagnosis[dias.size()]));

	//
	drn.setDirectionCode("CODE123");
	drn.setDirectionId("ID1234");
	drn.setDirectionLocation("GP1");

	//
	Employee emp = new Employee();
	emp.setEmployeeCode("CODE1");
	emp.setEmployeeName("Сидоров Сидор Сидорович");
	emp.setEmployeeType(Employee.TYPE_DOCTOR);

	drn.setDoctorDirect(emp);

	emp = new Employee();
	emp.setEmployeeCode("CODE2");
	emp.setEmployeeName("Иванов Сидор Сидорович");
	emp.setEmployeeType(Employee.TYPE_DOCTOR);

	drn.setDoctorPerformed(emp);

	//
	drn.setId(123l);

	//
	Patient pat = new Patient();
	pat.setId(123);
	pat.setPatientBirthDate("1974-03-30");
	pat.setPatientId("ID123");
	pat.setPatientName("Иванов Иван Иванович");
	pat.setPatientSex("M");
	drn.setPatient(pat);

	//
	ArrayList<Service> srvs = new ArrayList<Service>();

	Service srv = new Service();
	srv.setServiceAlias("ALIAS1");
	srv.setServiceCode("CODE1");
	srv.setServiceCount(2);
	srv.setServiceDescription("DESCR1");

	srvs.add(srv);

	srv = new Service();
	srv.setServiceAlias("ALIAS2");
	srv.setServiceCode("CODE2");
	srv.setServiceCount(3);
	srv.setServiceDescription("DESCR2");

	srvs.add(srv);

	drn.setServicesDirect(srvs.toArray(new Service[srvs.size()]));

	srv = new Service();
	srv.setServiceAlias("ALIAS3");
	srv.setServiceCode("CODE3");
	srv.setServiceCount(4);
	srv.setServiceDescription("DESCR3");

	srvs.add(srv);

	srv = new Service();
	srv.setServiceAlias("ALIAS4");
	srv.setServiceCode("CODE4");
	srv.setServiceCount(5);
	srv.setServiceDescription("DESCR4");

	srvs.add(srv);

	drn.setServicesPerformed(srvs.toArray(new Service[srvs.size()]));

	DirectionProxy proxy = ORMHelpers.getDirectionProxy(drn);

	assertEquals(drn.getDateDirection(), proxy.getDateDirection());
	assertEquals(drn.getDatePerformed(), proxy.getDatePerformed());
	assertEquals(drn.getDateTimeModified(), proxy.getDateTimeModified());
	assertEquals(drn.getDateTimePlanned(), proxy.getDateTimePlanned());
	assertEquals(drn.getDateTimeRemoved(), proxy.getDateTimeRemoved());
	assertEquals(drn.getDirectionCode(), proxy.getDirectionCode());
	assertEquals(drn.getDirectionId(), proxy.getDirectionId());
	assertEquals(drn.getDirectionLocation(), proxy.getDirectionLocation());

	//
	assertEquals(drn.getDevice().getManufacturerModelDescription(), proxy.getDevice()
		.getManufacturerModelDescription());
	assertEquals(drn.getDevice().getManufacturerModelName(), proxy.getDevice().getManufacturerModelName());
	assertEquals(drn.getDevice().getManufacturerModelTypeDescription(), proxy.getDevice()
		.getManufacturerModelTypeDescription());
	assertEquals(drn.getDevice().getModality(), proxy.getDevice().getModality());

	//
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisCode(), proxy.getDiagnosisDirect()[0].getDiagnosisCode());
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisDescription(), proxy.getDiagnosisDirect()[0]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisSubType(), proxy.getDiagnosisDirect()[0]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisType(), proxy.getDiagnosisDirect()[0].getDiagnosisType());

	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisCode(), proxy.getDiagnosisDirect()[1].getDiagnosisCode());
	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisDescription(), proxy.getDiagnosisDirect()[1]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisSubType(), proxy.getDiagnosisDirect()[1]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisType(), proxy.getDiagnosisDirect()[1].getDiagnosisType());

	//

	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisCode(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisCode());
	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisDescription(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisSubType(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisType(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisType());

	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisCode(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisCode());
	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisDescription(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisSubType(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisType(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisType());

	//
	assertEquals(drn.getDoctorDirect().getEmployeeCode(), proxy.getDoctorDirect().getEmployeeCode());
	assertEquals(drn.getDoctorDirect().getEmployeeName(), proxy.getDoctorDirect().getEmployeeName());
	assertEquals(drn.getDoctorDirect().getEmployeeType(), proxy.getDoctorDirect().getEmployeeType());

	//
	assertEquals(drn.getDoctorPerformed().getEmployeeCode(), proxy.getDoctorPerformed().getEmployeeCode());
	assertEquals(drn.getDoctorPerformed().getEmployeeName(), proxy.getDoctorPerformed().getEmployeeName());
	assertEquals(drn.getDoctorPerformed().getEmployeeType(), proxy.getDoctorPerformed().getEmployeeType());

	assertEquals(drn.getId(), proxy.getId());

	//
	assertEquals(drn.getPatient().getId(), proxy.getPatient().getId());
	assertEquals(drn.getPatient().getPatientBirthDate(), proxy.getPatient().getPatientBirthDate());
	assertEquals(drn.getPatient().getPatientId(), proxy.getPatient().getPatientId());
	assertEquals(drn.getPatient().getPatientName(), proxy.getPatient().getPatientName());
	assertEquals(drn.getPatient().getPatientSex(), proxy.getPatient().getPatientSex());
	assertEquals(drn.getPatient().getPatientShortName(), proxy.getPatient().getPatientShortName());

	//
	assertEquals(drn.getServicesDirect()[0].getServiceAlias(), proxy.getServicesDirect()[0].getServiceAlias());
	assertEquals(drn.getServicesDirect()[0].getServiceCode(), proxy.getServicesDirect()[0].getServiceCode());
	assertEquals(drn.getServicesDirect()[0].getServiceCount(), proxy.getServicesDirect()[0].getServiceCount());
	assertEquals(drn.getServicesDirect()[0].getServiceDescription(), proxy.getServicesDirect()[0]
		.getServiceDescription());

	assertEquals(drn.getServicesDirect()[1].getServiceAlias(), proxy.getServicesDirect()[1].getServiceAlias());
	assertEquals(drn.getServicesDirect()[1].getServiceCode(), proxy.getServicesDirect()[1].getServiceCode());
	assertEquals(drn.getServicesDirect()[1].getServiceCount(), proxy.getServicesDirect()[1].getServiceCount());
	assertEquals(drn.getServicesDirect()[1].getServiceDescription(), proxy.getServicesDirect()[1]
		.getServiceDescription());

	//

	assertEquals(drn.getServicesPerformed()[0].getServiceAlias(), proxy.getServicesPerformed()[0].getServiceAlias());
	assertEquals(drn.getServicesPerformed()[0].getServiceCode(), proxy.getServicesPerformed()[0].getServiceCode());
	assertEquals(drn.getServicesPerformed()[0].getServiceCount(), proxy.getServicesPerformed()[0].getServiceCount());
	assertEquals(drn.getServicesPerformed()[0].getServiceDescription(), proxy.getServicesPerformed()[0]
		.getServiceDescription());

	assertEquals(drn.getServicesPerformed()[1].getServiceAlias(), proxy.getServicesPerformed()[1].getServiceAlias());
	assertEquals(drn.getServicesPerformed()[1].getServiceCode(), proxy.getServicesPerformed()[1].getServiceCode());
	assertEquals(drn.getServicesPerformed()[1].getServiceCount(), proxy.getServicesPerformed()[1].getServiceCount());
	assertEquals(drn.getServicesPerformed()[1].getServiceDescription(), proxy.getServicesPerformed()[1]
		.getServiceDescription());

    }

    public void testGetDirection() {

	DirectionProxy proxy = new DirectionProxy();
	proxy.setDateDirection("2011-06-24");
	proxy.setDatePerformed("2011-06-25");
	proxy.setDateTimeModified("2011-06-25 12:00:00");
	proxy.setDateTimePlanned("2011-06-24 12:00:00");

	//
	ManufacturerDeviceProxy dev = new ManufacturerDeviceProxy();
	dev.setManufacturerModelDescription("РЕНЕКС ФЛЮОРОГРАФ");
	dev.setManufacturerModelName("РЕНЕКС");
	dev.setManufacturerModelTypeDescription("Для флюшки");
	dev.setModality("CR");

	proxy.setDevice(dev);

	//
	ArrayList<DiagnosisProxy> dias = new ArrayList<DiagnosisProxy>();

	DiagnosisProxy dia = new DiagnosisProxy();
	dia.setDiagnosisCode("Z01.11");
	dia.setDiagnosisDescription("DIAZ11");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
	dias.add(dia);

	dia = new DiagnosisProxy();
	dia.setDiagnosisCode("Z01.12");
	dia.setDiagnosisDescription("DIAZ12");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);
	dias.add(dia);

	proxy.setDiagnosisDirect(dias.toArray(new DiagnosisProxy[dias.size()]));

	dias = new ArrayList<DiagnosisProxy>();

	dia = new DiagnosisProxy();
	dia.setDiagnosisCode("Z01.13");
	dia.setDiagnosisDescription("DIAZ13");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
	dias.add(dia);

	dia = new DiagnosisProxy();
	dia.setDiagnosisCode("Z01.14");
	dia.setDiagnosisDescription("DIAZ14");
	dia.setDiagnosisSubType(Diagnosis.SUBTYPE_MAIN);
	dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);
	dias.add(dia);

	proxy.setDiagnosisPerformed(dias.toArray(new DiagnosisProxy[dias.size()]));

	//
	proxy.setDirectionCode("CODE123");
	proxy.setDirectionId("ID1234");
	proxy.setDirectionLocation("GP1");

	//
	EmployeeProxy emp = new EmployeeProxy();
	emp.setEmployeeCode("CODE1");
	emp.setEmployeeName("Сидоров Сидор Сидорович");
	emp.setEmployeeType(Employee.TYPE_DOCTOR);

	proxy.setDoctorDirect(emp);

	emp = new EmployeeProxy();
	emp.setEmployeeCode("CODE2");
	emp.setEmployeeName("Иванов Сидор Сидорович");
	emp.setEmployeeType(Employee.TYPE_DOCTOR);

	proxy.setDoctorPerformed(emp);

	//
	proxy.setId(123l);

	//
	PatientProxy pat = new PatientProxy();
	pat.setId(123);
	pat.setPatientBirthDate("1974-03-30");
	pat.setPatientId("ID123");
	pat.setPatientName("Иванов Иван Иванович");
	pat.setPatientSex("M");
	pat.setPatientShortName("ИВАИИ74");
	
	proxy.setPatient(pat);

	//
	ArrayList<ServiceProxy> srvs = new ArrayList<ServiceProxy>();

	ServiceProxy srv = new ServiceProxy();
	srv.setServiceAlias("ALIAS1");
	srv.setServiceCode("CODE1");
	srv.setServiceCount(2);
	srv.setServiceDescription("DESCR1");

	srvs.add(srv);

	srv = new ServiceProxy();
	srv.setServiceAlias("ALIAS2");
	srv.setServiceCode("CODE2");
	srv.setServiceCount(3);
	srv.setServiceDescription("DESCR2");

	srvs.add(srv);

	proxy.setServicesDirect(srvs.toArray(new ServiceProxy[srvs.size()]));

	srv = new ServiceProxy();
	srv.setServiceAlias("ALIAS3");
	srv.setServiceCode("CODE3");
	srv.setServiceCount(4);
	srv.setServiceDescription("DESCR3");

	srvs.add(srv);

	srv = new ServiceProxy();
	srv.setServiceAlias("ALIAS4");
	srv.setServiceCode("CODE4");
	srv.setServiceCount(5);
	srv.setServiceDescription("DESCR4");

	srvs.add(srv);

	proxy.setServicesPerformed(srvs.toArray(new ServiceProxy[srvs.size()]));
	
	
	Direction drn = ORMHelpers.getDirection(proxy);

	assertEquals(drn.getDateDirection(), proxy.getDateDirection());
	assertEquals(drn.getDatePerformed(), proxy.getDatePerformed());
	assertEquals(drn.getDateTimeModified(), proxy.getDateTimeModified());
	assertEquals(drn.getDateTimePlanned(), proxy.getDateTimePlanned());
	assertEquals(drn.getDateTimeRemoved(), proxy.getDateTimeRemoved());
	assertEquals(drn.getDirectionCode(), proxy.getDirectionCode());
	assertEquals(drn.getDirectionId(), proxy.getDirectionId());
	assertEquals(drn.getDirectionLocation(), proxy.getDirectionLocation());

	//
	assertEquals(drn.getDevice().getManufacturerModelDescription(), proxy.getDevice()
		.getManufacturerModelDescription());
	assertEquals(drn.getDevice().getManufacturerModelName(), proxy.getDevice().getManufacturerModelName());
	assertEquals(drn.getDevice().getManufacturerModelTypeDescription(), proxy.getDevice()
		.getManufacturerModelTypeDescription());
	assertEquals(drn.getDevice().getModality(), proxy.getDevice().getModality());

	//
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisCode(), proxy.getDiagnosisDirect()[0].getDiagnosisCode());
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisDescription(), proxy.getDiagnosisDirect()[0]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisSubType(), proxy.getDiagnosisDirect()[0]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisType(), proxy.getDiagnosisDirect()[0].getDiagnosisType());

	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisCode(), proxy.getDiagnosisDirect()[1].getDiagnosisCode());
	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisDescription(), proxy.getDiagnosisDirect()[1]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisSubType(), proxy.getDiagnosisDirect()[1]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisType(), proxy.getDiagnosisDirect()[1].getDiagnosisType());

	//

	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisCode(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisCode());
	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisDescription(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisSubType(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisPerformed()[0].getDiagnosisType(), proxy.getDiagnosisPerformed()[0]
		.getDiagnosisType());

	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisCode(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisCode());
	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisDescription(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisDescription());
	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisSubType(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisSubType());
	assertEquals(drn.getDiagnosisPerformed()[1].getDiagnosisType(), proxy.getDiagnosisPerformed()[1]
		.getDiagnosisType());

	//
	assertEquals(drn.getDoctorDirect().getEmployeeCode(), proxy.getDoctorDirect().getEmployeeCode());
	assertEquals(drn.getDoctorDirect().getEmployeeName(), proxy.getDoctorDirect().getEmployeeName());
	assertEquals(drn.getDoctorDirect().getEmployeeType(), proxy.getDoctorDirect().getEmployeeType());

	//
	assertEquals(drn.getDoctorPerformed().getEmployeeCode(), proxy.getDoctorPerformed().getEmployeeCode());
	assertEquals(drn.getDoctorPerformed().getEmployeeName(), proxy.getDoctorPerformed().getEmployeeName());
	assertEquals(drn.getDoctorPerformed().getEmployeeType(), proxy.getDoctorPerformed().getEmployeeType());

	assertEquals(drn.getId(), proxy.getId());

	//
	assertEquals(drn.getPatient().getId(), proxy.getPatient().getId());
	assertEquals(drn.getPatient().getPatientBirthDate(), proxy.getPatient().getPatientBirthDate());
	assertEquals(drn.getPatient().getPatientId(), proxy.getPatient().getPatientId());
	assertEquals(drn.getPatient().getPatientName(), proxy.getPatient().getPatientName());
	assertEquals(drn.getPatient().getPatientSex(), proxy.getPatient().getPatientSex());
	assertEquals(drn.getPatient().getPatientShortName(), proxy.getPatient().getPatientShortName());

	//
	assertEquals(drn.getServicesDirect()[0].getServiceAlias(), proxy.getServicesDirect()[0].getServiceAlias());
	assertEquals(drn.getServicesDirect()[0].getServiceCode(), proxy.getServicesDirect()[0].getServiceCode());
	assertEquals(drn.getServicesDirect()[0].getServiceCount(), proxy.getServicesDirect()[0].getServiceCount());
	assertEquals(drn.getServicesDirect()[0].getServiceDescription(), proxy.getServicesDirect()[0]
		.getServiceDescription());

	assertEquals(drn.getServicesDirect()[1].getServiceAlias(), proxy.getServicesDirect()[1].getServiceAlias());
	assertEquals(drn.getServicesDirect()[1].getServiceCode(), proxy.getServicesDirect()[1].getServiceCode());
	assertEquals(drn.getServicesDirect()[1].getServiceCount(), proxy.getServicesDirect()[1].getServiceCount());
	assertEquals(drn.getServicesDirect()[1].getServiceDescription(), proxy.getServicesDirect()[1]
		.getServiceDescription());

	//

	assertEquals(drn.getServicesPerformed()[0].getServiceAlias(), proxy.getServicesPerformed()[0].getServiceAlias());
	assertEquals(drn.getServicesPerformed()[0].getServiceCode(), proxy.getServicesPerformed()[0].getServiceCode());
	assertEquals(drn.getServicesPerformed()[0].getServiceCount(), proxy.getServicesPerformed()[0].getServiceCount());
	assertEquals(drn.getServicesPerformed()[0].getServiceDescription(), proxy.getServicesPerformed()[0]
		.getServiceDescription());

	assertEquals(drn.getServicesPerformed()[1].getServiceAlias(), proxy.getServicesPerformed()[1].getServiceAlias());
	assertEquals(drn.getServicesPerformed()[1].getServiceCode(), proxy.getServicesPerformed()[1].getServiceCode());
	assertEquals(drn.getServicesPerformed()[1].getServiceCount(), proxy.getServicesPerformed()[1].getServiceCount());
	assertEquals(drn.getServicesPerformed()[1].getServiceDescription(), proxy.getServicesPerformed()[1]
		.getServiceDescription());


    }

}
