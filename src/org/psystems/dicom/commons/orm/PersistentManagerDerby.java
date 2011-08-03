package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.psystems.dicom.browser.client.component.WorkListPanel;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.commons.orm.entity.DataException;
import org.psystems.dicom.commons.orm.entity.Diagnosis;
import org.psystems.dicom.commons.orm.entity.Direction;
import org.psystems.dicom.commons.orm.entity.Employee;
import org.psystems.dicom.commons.orm.entity.ManufacturerDevice;
import org.psystems.dicom.commons.orm.entity.Patient;
import org.psystems.dicom.commons.orm.entity.QueryDirection;
import org.psystems.dicom.commons.orm.entity.QueryStudy;
import org.psystems.dicom.commons.orm.entity.Service;
import org.psystems.dicom.commons.orm.entity.Study;

public class PersistentManagerDerby {

    private Connection connection;
    // TODO Убрать в конфиг!!!
    private int maxResponsRecords = 300;// Максимальное количество возвращаемых

    // записей

    /**
     * @param connection
     */
    public PersistentManagerDerby(Connection connection) {
	super();
	this.connection = connection;
    }

    public long pesistentDirection(Direction drn) throws DataException {
	return makePesistentDirection((Direction) drn);
    }

    /**
     * Сохранение исследования TODO Не реализовано !!!
     * 
     * @param study
     * @return
     */
    public long makePesistentStudy(Study study) {
	// TODO Auto-generated method stub
	return 0;
    }

    /**
     * Сохранение направления
     * 
     * @param drn
     * @throws DataException
     */
    public long makePesistentDirection(Direction drn) throws DataException {

	PreparedStatement pstmt = null;
	String sql = null;

	drn.chechEntity();

	if (drn.getPatient() == null) {
	    throw new DataException("Patient could not be null.");
	} else {
	    if (drn.getPatient().getPatientName() == null)
		throw new DataException("Patient Name could not be null.");
	    if (drn.getPatient().getPatientSex() == null)
		throw new DataException("Patient Sex could not be null.");
	    if (drn.getPatient().getPatientBirthDate() == null)
		throw new DataException("Patient Birth Date could not be null.");
	    if (drn.getPatient().getPatientName() == null)
		throw new DataException("Patient Name could not be null.");

	}

	long resultId = 0;

	try {

	    connection.setAutoCommit(false);

	    if (drn.getId() == null) { // делаем вставку
		sql = "INSERT INTO WEBDICOM.DIRECTION (" + "DIRECTION_ID," // 1
			+ "DOCTOR_DIRECT_NAME," // 2
			+ "DOCTOR_DIRECT_CODE," // 3
			+ "DATE_DIRECTION," // 4
			+ "DEVICE," // 5
			+ "DIRECTION_DATE_PLANNED," // 6
			+ "DOCTOR_PERFORMED_NAME," // 7
			+ "DOCTOR_PERFORMED_CODE," // 8
			+ "DIRECTION_CODE," // 9
			+ "DIRECTION_LOCATION," // 10
			+ "DATE_PERFORMED," // 11
			+ "PATIENT_ID," // 12
			+ "PATIENT_NAME," // 13
			+ "PATIENT_SEX," // 14
			+ "PATIENT_BIRTH_DATE," // 15
			+ "DATE_MODIFIED," // 16
			+ "REMOVED" // 17
			+ ") VALUES " + "(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?)";

		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, drn.getDirectionId());

		if (drn.getDoctorDirect() != null)
		    pstmt.setString(2, drn.getDoctorDirect().getEmployeeName());
		else
		    pstmt.setNull(2, java.sql.Types.VARCHAR);

		if (drn.getDoctorDirect() != null)
		    pstmt.setString(3, drn.getDoctorDirect().getEmployeeCode());
		else
		    pstmt.setNull(3, java.sql.Types.VARCHAR);

		if (drn.getDateDirection() != null)
		    pstmt.setDate(4, java.sql.Date.valueOf(drn.getDateDirection()));
		else
		    pstmt.setNull(4, java.sql.Types.DATE);

		if (drn.getDevice() != null)
		    pstmt.setString(5, drn.getDevice().getManufacturerModelName());
		else
		    pstmt.setNull(5, java.sql.Types.VARCHAR);

		try {
		    if (drn.getDateTimePlanned() != null)
			pstmt.setTimestamp(6, new Timestamp(java.sql.Timestamp.valueOf(drn.getDateTimePlanned())
				.getTime()));
		    else
			pstmt.setNull(6, java.sql.Types.DATE);

		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Time Planned wrong format: " + drn.getDateTimePlanned(), ex);
		}

		if (drn.getDoctorPerformed() != null) {
		    pstmt.setString(7, drn.getDoctorPerformed().getEmployeeName());
		    pstmt.setString(8, drn.getDoctorPerformed().getEmployeeCode());
		} else {
		    pstmt.setNull(7, java.sql.Types.VARCHAR);
		    pstmt.setNull(8, java.sql.Types.VARCHAR);
		}

		pstmt.setString(9, drn.getDirectionCode());
		pstmt.setString(10, drn.getDirectionLocation());

		try {
		    if (drn.getDatePerformed() != null)
			pstmt.setDate(11, java.sql.Date.valueOf(drn.getDatePerformed()));
		    else
			pstmt.setNull(11, java.sql.Types.DATE);

		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Performed wrong format: " + drn.getDatePerformed(), ex);
		}

		if (drn.getPatient() != null) {
		    pstmt.setString(12, drn.getPatient().getPatientId());
		    pstmt.setString(13, drn.getPatient().getPatientName());
		    pstmt.setString(14, drn.getPatient().getPatientSex());
		} else {
		    pstmt.setNull(12, java.sql.Types.VARCHAR);
		    pstmt.setNull(13, java.sql.Types.VARCHAR);
		    pstmt.setNull(14, java.sql.Types.VARCHAR);
		}

		try {
		    if (drn.getPatient() != null && drn.getPatient().getPatientBirthDate() != null) {
			pstmt.setDate(15, java.sql.Date.valueOf(drn.getPatient().getPatientBirthDate()));
		    } else {
			pstmt.setNull(15, java.sql.Types.DATE);
		    }
		} catch (IllegalArgumentException ex) {
		    throw new DataException("Patient field Birth Date wrong format: "
			    + drn.getPatient().getPatientBirthDate(), ex);
		}

		pstmt.setTimestamp(16, new Timestamp(new java.util.Date().getTime()));// sysdate

		try {
		    if (drn.getDateTimeRemoved() != null)
			pstmt.setTimestamp(17, new Timestamp(java.sql.Timestamp.valueOf(drn.getDateTimeRemoved())
				.getTime()));
		    else
			pstmt.setNull(17, java.sql.Types.TIMESTAMP);
		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Removed wrong format: " + drn.getDateTimeRemoved(), ex);
		}

		int count = pstmt.executeUpdate();

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("values IDENTITY_VAL_LOCAL()");
		while (rs.next()) {
		    resultId = rs.getLong("1");
		}

	    } else { // Делаем update

		// TODO Сделать корректную проверку на оюновление направления в
		// БД.
		// по каким полям. по ID? или directionId?...

		sql = "UPDATE WEBDICOM.DIRECTION SET " + "DIRECTION_ID = ?," // 1
			+ "DOCTOR_DIRECT_NAME = ?," // 2
			+ "DOCTOR_DIRECT_CODE = ?," // 3
			+ "DATE_DIRECTION = ?," // 4
			+ "DEVICE = ?," // 5
			+ "DIRECTION_DATE_PLANNED = ?," // 6
			+ "DOCTOR_PERFORMED_NAME = ?," // 7
			+ "DOCTOR_PERFORMED_CODE = ?," // 8
			+ "DIRECTION_CODE = ?," // 9
			+ "DIRECTION_LOCATION = ?," // 10
			+ "DATE_PERFORMED = ?," // 11
			+ "PATIENT_ID = ?," // 12
			+ "PATIENT_NAME = ?," // 13
			+ "PATIENT_SEX = ?," // 14
			+ "PATIENT_BIRTH_DATE = ?," // 15
			+ "DATE_MODIFIED = ?," // 16
			+ "REMOVED = ?" // 17
			+ " WHERE ID = ?"; // 18

		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, drn.getDirectionId());

		if (drn.getDoctorDirect() != null)
		    pstmt.setString(2, drn.getDoctorDirect().getEmployeeName());
		else
		    pstmt.setNull(2, java.sql.Types.VARCHAR);

		if (drn.getDoctorDirect() != null)
		    pstmt.setString(3, drn.getDoctorDirect().getEmployeeCode());
		else
		    pstmt.setNull(3, java.sql.Types.VARCHAR);

		try {
		    if (drn.getDateDirection() != null)
			pstmt.setDate(4, java.sql.Date.valueOf(drn.getDateDirection()));
		    else
			pstmt.setNull(4, java.sql.Types.DATE);

		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Direction wrong format: " + drn.getDateDirection(), ex);
		}

		if (drn.getDevice() != null)
		    pstmt.setString(5, drn.getDevice().getManufacturerModelName());
		else
		    pstmt.setNull(5, java.sql.Types.VARCHAR);

		try {
		    if (drn.getDateTimePlanned() != null)
			pstmt.setTimestamp(6, new Timestamp(java.sql.Timestamp.valueOf(drn.getDateTimePlanned())
				.getTime()));
		    else
			pstmt.setNull(6, java.sql.Types.DATE);
		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Time Planned wrong format: " + drn.getDateTimePlanned(), ex);
		}

		if (drn.getDoctorPerformed() != null) {
		    pstmt.setString(7, drn.getDoctorPerformed().getEmployeeName());
		    pstmt.setString(8, drn.getDoctorPerformed().getEmployeeCode());
		} else {
		    pstmt.setNull(7, java.sql.Types.VARCHAR);
		    pstmt.setNull(8, java.sql.Types.VARCHAR);
		}

		pstmt.setString(9, drn.getDirectionCode());
		pstmt.setString(10, drn.getDirectionLocation());

		try {
		    if (drn.getDatePerformed() != null)
			pstmt.setDate(11, java.sql.Date.valueOf(drn.getDatePerformed()));
		    else
			pstmt.setNull(11, java.sql.Types.DATE);
		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Performed wrong format: " + drn.getDatePerformed(), ex);
		}

		if (drn.getPatient() != null) {
		    pstmt.setString(12, drn.getPatient().getPatientId());
		    pstmt.setString(13, drn.getPatient().getPatientName());
		    pstmt.setString(14, drn.getPatient().getPatientSex());
		} else {
		    pstmt.setNull(12, java.sql.Types.VARCHAR);
		    pstmt.setNull(13, java.sql.Types.VARCHAR);
		    pstmt.setNull(14, java.sql.Types.VARCHAR);
		}

		try {
		    if (drn.getPatient() != null && drn.getPatient().getPatientBirthDate() != null) {
			pstmt.setDate(15, java.sql.Date.valueOf(drn.getPatient().getPatientBirthDate()));
		    } else {
			pstmt.setNull(15, java.sql.Types.DATE);
		    }
		} catch (IllegalArgumentException ex) {
		    throw new DataException("Patient field Birth Date wrong format: "
			    + drn.getPatient().getPatientBirthDate(), ex);
		}

		pstmt.setTimestamp(16, new Timestamp(new java.util.Date().getTime()));// sysdate

		try {
		    if (drn.getDateTimeRemoved() != null)
			pstmt.setTimestamp(17, new Timestamp(java.sql.Timestamp.valueOf(drn.getDateTimeRemoved())
				.getTime()));
		    else
			pstmt.setNull(17, java.sql.Types.TIMESTAMP);
		} catch (IllegalArgumentException ex) {
		    throw new DataException("field Date Removed wrong format: " + drn.getDateTimeRemoved(), ex);
		}

		pstmt.setLong(18, drn.getId());
		int count = pstmt.executeUpdate();
		resultId = drn.getId();

	    }

	    // удаляем-обновляем диагнозы и услуги
	    pstmt.close();

	    sql = "DELETE FROM WEBDICOM.DIRECTION_DIAGNOSIS WHERE FID_DIRECTION = ?";
	    pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, resultId);
	    int count = pstmt.executeUpdate();

	    pstmt.close();
	    sql = "DELETE FROM WEBDICOM.DIRECTION_SERVICE WHERE FID_DIRECTION = ?";
	    pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, resultId);
	    count = pstmt.executeUpdate();

	    // Сохраняем диагнозы
	    pstmt.close();
	    sql = "INSERT INTO WEBDICOM.DIRECTION_DIAGNOSIS (" + "FID_DIRECTION," + // 1
		    "TYPE_ON_DIRECTION," + // 2
		    "DIAGNOSIS_CODE," + // 3
		    "DIAGNOSIS_TYPE," + // 4
		    "DIAGNOSIS_SUBTYPE," + // 5
		    "DIAGNOSIS_DESCRIPTION" + // 6
		    ") VALUES (?,?,?,?,?,?)";
	    pstmt = connection.prepareStatement(sql);

	    if (drn.getDiagnosisDirect() != null)
		for (Diagnosis diagnosis : drn.getDiagnosisDirect()) {
		    pstmt.setLong(1, resultId);
		    pstmt.setString(2, "D");
		    pstmt.setString(3, diagnosis.getDiagnosisCode());
		    pstmt.setString(4, diagnosis.getDiagnosisType());
		    pstmt.setString(5, diagnosis.getDiagnosisSubType());
		    pstmt.setString(6, diagnosis.getDiagnosisDescription());
		    count = pstmt.executeUpdate();
		}

	    if (drn.getDiagnosisPerformed() != null)
		for (Diagnosis diagnosis : drn.getDiagnosisPerformed()) {
		    pstmt.setLong(1, resultId);
		    pstmt.setString(2, "P");
		    pstmt.setString(3, diagnosis.getDiagnosisCode());
		    pstmt.setString(4, diagnosis.getDiagnosisType());
		    pstmt.setString(5, diagnosis.getDiagnosisSubType());
		    pstmt.setString(6, diagnosis.getDiagnosisDescription());
		    count = pstmt.executeUpdate();
		}

	    // Сохраняем услуги
	    pstmt.close();
	    sql = "INSERT INTO WEBDICOM.DIRECTION_SERVICE (" + "FID_DIRECTION," + // 1
		    "TYPE_ON_DIRECTION," + // 2
		    "SERVICE_CODE," + // 3
		    "SERVICE_ALIAS," + // 4
		    "SERVICE_DESCRIPTION," + // 5
		    "SERVICE_COUNT" + // 6
		    ") VALUES (?,?,?,?,?,?)";
	    pstmt = connection.prepareStatement(sql);

	    if (drn.getServicesDirect() != null)
		for (Service srv : drn.getServicesDirect()) {
		    pstmt.setLong(1, resultId);
		    pstmt.setString(2, "D");
		    pstmt.setString(3, srv.getServiceCode());
		    pstmt.setString(4, srv.getServiceAlias());
		    pstmt.setString(5, srv.getServiceDescription());
		    pstmt.setInt(6, srv.getServiceCount());
		    count = pstmt.executeUpdate();
		}
	    if (drn.getServicesPerformed() != null)
		for (Service srv : drn.getServicesPerformed()) {
		    pstmt.setLong(1, resultId);
		    pstmt.setString(2, "P");
		    pstmt.setString(3, srv.getServiceCode());
		    pstmt.setString(4, srv.getServiceAlias());
		    pstmt.setString(5, srv.getServiceDescription());
		    pstmt.setInt(6, srv.getServiceCount());
		    count = pstmt.executeUpdate();
		}

	    connection.commit();

	} catch (SQLException e) {
	    throw new DataException("SQL Error " + e.getMessage(), e);
	} catch (IllegalArgumentException e) {
	    throw new DataException("Internal error " + e.getMessage(), e);
	} finally {

	    try {
		if (pstmt != null)
		    pstmt.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
	return resultId;
	// TODO Auto-generated method stub

    }

    /**
     * Получение экземпляра Направления из Record Set
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private Direction getDirectionFromRs(ResultSet rs) throws SQLException {
	Direction drn = new Direction();
	drn.setId(rs.getLong("ID"));
	drn.setDirectionId(rs.getString("DIRECTION_ID"));
	Employee doctorDirect = new Employee();
	doctorDirect.setEmployeeCode(rs.getString("DOCTOR_DIRECT_CODE"));
	doctorDirect.setEmployeeName(rs.getString("DOCTOR_DIRECT_NAME"));
	doctorDirect.setEmployeeType(Employee.TYPE_DOCTOR);
	drn.setDoctorDirect(doctorDirect);

	drn.setDateDirection(ORMUtil.utilDateToSQLDateString(rs.getDate("DATE_DIRECTION")));
	ManufacturerDevice dev = new ManufacturerDevice();
	dev.setManufacturerModelName(rs.getString("DEVICE"));
	drn.setDevice(dev);
	// TODO Сделать rs.getTimestamp???
	drn.setDateTimePlanned(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getTimestamp("DIRECTION_DATE_PLANNED")));

	if (rs.getString("DOCTOR_PERFORMED_NAME") != null) {
	    Employee doctorPerformed = new Employee();
	    doctorPerformed.setEmployeeCode(rs.getString("DOCTOR_PERFORMED_CODE"));
	    doctorPerformed.setEmployeeName(rs.getString("DOCTOR_PERFORMED_NAME"));
	    doctorPerformed.setEmployeeType(Employee.TYPE_DOCTOR);
	    drn.setDoctorPerformed(doctorPerformed);
	}

	drn.setDirectionCode(rs.getString("DIRECTION_CODE"));
	drn.setDirectionLocation(rs.getString("DIRECTION_LOCATION"));

	drn.setDatePerformed(ORMUtil.utilDateToSQLDateString(rs.getDate("DATE_PERFORMED")));

	Patient patient = new Patient();
	patient.setPatientId(rs.getString("PATIENT_ID"));
	patient.setPatientName(rs.getString("PATIENT_NAME"));
	// TODO проверить, корректно ли возвращается дата !!!

	patient.setPatientBirthDate(ORMUtil.utilDateToSQLDateString(rs.getDate("PATIENT_BIRTH_DATE")));
	patient.setPatientSex(rs.getString("PATIENT_SEX"));
	drn.setPatient(patient);

	// TODO Сделать rs.getTimestamp???
	drn.setDateTimeModified(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getTimestamp("DATE_MODIFIED")));
	// TODO Сделать rs.getTimestamp???
	drn.setDateTimeRemoved(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getTimestamp("REMOVED")));

	// наполняем диагнозами
	setDirectionDiagnosis(drn);
	// наполняем услугами
	setDirectionServices(drn);
	return drn;
    }

    /**
     * Дополнение направления диагнозами
     * 
     * @param drn
     * @return
     * @throws SQLException
     */
    private Direction setDirectionDiagnosis(Direction drn) throws SQLException {
	PreparedStatement pstmt = null;
	try {
	    pstmt = connection.prepareStatement("SELECT * FROM WEBDICOM.DIRECTION_DIAGNOSIS WHERE FID_DIRECTION = ? ");
	    pstmt.setLong(1, drn.getId());
	    ResultSet rs = pstmt.executeQuery();

	    ArrayList<Diagnosis> diasDirrect = new ArrayList<Diagnosis>();
	    ArrayList<Diagnosis> diasPerformed = new ArrayList<Diagnosis>();
	    while (rs.next()) {

		Diagnosis dia = new Diagnosis();
		dia.setDiagnosisCode(rs.getString("DIAGNOSIS_CODE"));
		dia.setDiagnosisType(rs.getString("DIAGNOSIS_TYPE"));
		dia.setDiagnosisSubType(rs.getString("DIAGNOSIS_SUBTYPE"));
		dia.setDiagnosisDescription(rs.getString("DIAGNOSIS_DESCRIPTION"));

		// направленные
		if (rs.getString("TYPE_ON_DIRECTION").equals("D")) {
		    diasDirrect.add(dia);

		}// выполненные
		else if (rs.getString("TYPE_ON_DIRECTION").equals("P")) {
		    diasPerformed.add(dia);
		}
	    }

	    drn.setDiagnosisDirect(diasDirrect.toArray(new Diagnosis[diasDirrect.size()]));
	    drn.setDiagnosisPerformed(diasPerformed.toArray(new Diagnosis[diasPerformed.size()]));
	    return drn;

	} finally {
	    if (pstmt != null)
		pstmt.close();
	}
    }

    /**
     * Дополнение направления услугами
     * 
     * @param drn
     * @return
     * @throws SQLException
     */
    private Direction setDirectionServices(Direction drn) throws SQLException {
	PreparedStatement pstmt = null;
	try {
	    pstmt = connection.prepareStatement("SELECT * FROM WEBDICOM.DIRECTION_SERVICE WHERE FID_DIRECTION = ? ");
	    pstmt.setLong(1, drn.getId());
	    ResultSet rs = pstmt.executeQuery();

	    ArrayList<Service> servDirrect = new ArrayList<Service>();
	    ArrayList<Service> servPerformed = new ArrayList<Service>();
	    while (rs.next()) {

		Service srv = new Service();
		srv.setServiceCode(rs.getString("SERVICE_CODE"));
		srv.setServiceAlias(rs.getString("SERVICE_ALIAS"));
		srv.setServiceDescription(rs.getString("SERVICE_DESCRIPTION"));
		srv.setServiceCount(rs.getInt("SERVICE_COUNT"));

		// направленные
		if (rs.getString("TYPE_ON_DIRECTION").equals("D")) {
		    servDirrect.add(srv);

		}// выполненные
		else if (rs.getString("TYPE_ON_DIRECTION").equals("P")) {
		    servPerformed.add(srv);
		}
	    }

	    drn.setServicesDirect(servDirrect.toArray(new Service[servDirrect.size()]));
	    drn.setServicesPerformed(servPerformed.toArray(new Service[servPerformed.size()]));
	    return drn;

	} finally {
	    if (pstmt != null)
		pstmt.close();
	}
    }

    public Direction getDirectionByID(Long id) throws DataException {

	Direction drn = new Direction();
	PreparedStatement pstmt = null;

	try {
	    pstmt = connection.prepareStatement("SELECT * FROM WEBDICOM.DIRECTION WHERE ID = ? ");
	    pstmt.setLong(1, id);
	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		drn = getDirectionFromRs(rs);
	    }

	    return drn;
	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (pstmt != null)
		    pstmt.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
    }

    public Direction getDirectionByDirectionId(String internalID) throws DataException {

	Direction drn = new Direction();
	PreparedStatement pstmt = null;

	try {
	    pstmt = connection.prepareStatement("SELECT * FROM WEBDICOM.DIRECTION WHERE DIRECTION_ID = ? ");
	    pstmt.setString(1, internalID);
	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		drn = getDirectionFromRs(rs);
	    }
	    return drn;
	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (pstmt != null)
		    pstmt.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
    }

    public Study getStudyByUID(String uid) throws DataException {
	// TODO Auto-generated method stub
	return null;
    }

    public ArrayList<Direction> queryDirections(QueryDirection request) throws DataException {

	if (request == null)
	    throw new IllegalArgumentException("Request could not be null.");

	// проверяем валидность направления
	request.chechEntity();

	ArrayList<Direction> result = new ArrayList<Direction>();
	PreparedStatement pstmt = null;
	String sql = "SELECT * FROM WEBDICOM.DIRECTION WHERE ";

	try {

	    // Разбор полей

	    int counterArguments = 0;
	    if (request.getId() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " ID = ? ";
	    }
	    if (request.getDirectionId() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DIRECTION_ID = ? ";
	    }
	    if (request.getDateDirection() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DATE_DIRECTION = ? ";
	    }
	    if (request.getPatientBirthDate() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " PATIENT_BIRTH_DATE = ? ";
	    }
	    if (request.getPatientId() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " PATIENT_ID = ? ";
	    }
	    if (request.getPatientName() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " PATIENT_NAME = ? ";
	    }
	    if (request.getPatientSex() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " PATIENT_SEX = ? ";
	    }

	    //

	    if (request.getManufacturerDevice() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DEVICE = ? ";
	    }

	    if (request.getDateTimePlannedBegin() != null && request.getDateTimePlannedEnd() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DIRECTION_DATE_PLANNED BETWEEN ? AND ?";
	    }

	    if (request.getDirectionLocation() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DIRECTION_LOCATION = ? ";
	    }

	    if (request.getDoctorDirectName() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DOCTOR_DIRECT_NAME = ? ";
	    }

	    if (request.getDoctorDirectCode() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DOCTOR_DIRECT_CODE = ? ";
	    }

	    if (request.getDoctorPerformedName() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DOCTOR_PERFORMED_NAME = ? ";
	    }

	    if (request.getDoctorPerformedCode() != null) {
		if (counterArguments++ > 0)
		    sql += " AND ";
		sql += " DOCTOR_PERFORMED_CODE = ? ";
	    }

	    if (request.getPatientShortName() != null) {
		throw new DataException("Search for PatientShortName Not implemented yet!");
	    }

	    if (counterArguments == 0)
		throw new DataException("All query arguments empty! Set any argument's");

	    pstmt = connection.prepareStatement(sql);
	    counterArguments = 1;

	    if (request.getId() != null) {
		pstmt.setLong(counterArguments++, request.getId());
	    }
	    if (request.getDirectionId() != null) {
		pstmt.setString(counterArguments++, request.getDirectionId());
	    }
	    if (request.getDateDirection() != null) {
		pstmt.setDate(counterArguments++, java.sql.Date.valueOf(request.getDateDirection()));
	    }
	    if (request.getPatientBirthDate() != null) {
		pstmt.setDate(counterArguments++, java.sql.Date.valueOf(request.getPatientBirthDate()));
	    }
	    if (request.getPatientId() != null) {
		pstmt.setString(counterArguments++, request.getPatientId());
	    }
	    if (request.getPatientName() != null) {
		pstmt.setString(counterArguments++, request.getPatientName());
	    }
	    if (request.getPatientSex() != null) {
		pstmt.setString(counterArguments++, request.getPatientSex());
	    }

	    //

	    if (request.getManufacturerDevice() != null) {
		pstmt.setString(counterArguments++, request.getManufacturerDevice());
	    }
	    if (request.getDateTimePlannedBegin() != null && request.getDateTimePlannedEnd() != null) {
		pstmt.setTimestamp(counterArguments++, java.sql.Timestamp.valueOf(request.getDateTimePlannedBegin()));
		pstmt.setTimestamp(counterArguments++, java.sql.Timestamp.valueOf(request.getDateTimePlannedEnd()));
	    }
	    if (request.getDirectionLocation() != null) {
		pstmt.setString(counterArguments++, request.getDirectionLocation());
	    }
	    if (request.getDoctorDirectName() != null) {
		pstmt.setString(counterArguments++, request.getDoctorDirectName());
	    }
	    if (request.getDoctorDirectCode() != null) {
		pstmt.setString(counterArguments++, request.getDoctorDirectCode());
	    }
	    if (request.getDoctorPerformedName() != null) {
		pstmt.setString(counterArguments++, request.getDoctorPerformedName());
	    }
	    if (request.getDoctorPerformedCode() != null) {
		pstmt.setString(counterArguments++, request.getDoctorPerformedCode());
	    }

	    ResultSet rs = pstmt.executeQuery();
	    int counter = 0;
	    while (rs.next()) {
		result.add(getDirectionFromRs(rs));
		if (counter > maxResponsRecords)
		    break;
	    }
	    return result;
	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (pstmt != null)
		    pstmt.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}

    }

    /**
     * Получение экземпляра исследования из Record Set
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private Study getStudyFromRs(ResultSet rs) throws DataException {

	try {
	    Study study = new Study();
	    study.setId(rs.getLong("ID"));
	    // study.setDirectionID(rs.getLong("FID_DIRECTION"));
	    study.setStudyInstanceUID(rs.getString("STUDY_UID"));
	    study.setStudyModality(rs.getString("STUDY_MODALITY"));
	    study.setStudyType(rs.getString("STUDY_TYPE"));
	    study.setStudyDescription(rs.getString("STUDY_DESCRIPTION"));

	    study.setStudyDate(ORMUtil.utilDateToSQLDateString(rs.getDate("STUDY_DATE")));

	    study.setManufacturerModelUID(rs.getString("STUDY_MANUFACTURER_UID"));
	    study.setManufacturerModelName(rs.getString("STUDY_MANUFACTURER_MODEL_NAME"));
	    study.setStudyDoctor(rs.getString("STUDY_DOCTOR"));
	    study.setStudyOperator(rs.getString("STUDY_OPERATOR"));
	    study.setStudyViewprotocol(rs.getString("STUDY_VIEW_PROTOCOL"));

	    study.setStudyViewprotocolDate(ORMUtil.utilDateToSQLDateString(rs.getDate("STUDY_VIEW_PROTOCOL_DATE")));

	    study.setStudyId(rs.getString("STUDY_ID"));
	    study.setPatientName(rs.getString("PATIENT_NAME"));
	    study.setPatientShortName(rs.getString("PATIENT_SHORTNAME"));
	    study.setPatientSex(rs.getString("PATIENT_SEX"));

	    study.setPatientBirthDate(ORMUtil.utilDateToSQLDateString(rs.getDate("PATIENT_BIRTH_DATE")));

	    study.setPatientId(rs.getString("PATIENT_ID"));
	    study.setStudyResult(rs.getString("STUDY_RESULT"));
	    study.setStudyUrl("");// TODO сделать!!
	    study.setDcmFilesId(new Long[] { 1l, 2l, 3l });// TODO сделать!!

	    study.setStudyDateTimeModify(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getDate("DATE_MODIFIED")));
	    study.setStudyDateTimeRemoved(null);
	    if (rs.getTimestamp("REMOVED") != null)
		study.setStudyDateTimeRemoved(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getDate("REMOVED")));

	    if (rs.getString("FID_DIRECTION") != null) {
		study.setDirection(getDirectionByID(rs.getLong("FID_DIRECTION")));
	    }

	    return study;

	} catch (SQLException ex) {
	    throw new DataException(ex);
	}
    }

    public Study getStudyByID(Long id) throws DataException {
	PreparedStatement psSelect = null;
	String sql = "SELECT * FROM WEBDICOM.STUDY WHERE ID = ?";

	try {

	    psSelect = connection.prepareStatement(sql);
	    psSelect.setLong(1, id);
	    ResultSet rs = psSelect.executeQuery();

	    while (rs.next()) {
		return getStudyFromRs(rs);
	    }
	    rs.close();

	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
	return null;
    }

    /**
     * Получение исследований по ID направления
     * 
     * @param id
     * @return
     * @throws DataException
     */
    public Study[] getStudiesByDirectionID(Long id) throws DataException {
	PreparedStatement psSelect = null;
	String sql = "SELECT * FROM WEBDICOM.STUDY WHERE FID_DIRECTION = ?";
	ArrayList<Study> studies = new ArrayList<Study>();
	try {

	    psSelect = connection.prepareStatement(sql);
	    psSelect.setLong(1, id);
	    ResultSet rs = psSelect.executeQuery();

	    while (rs.next())
		studies.add(getStudyFromRs(rs));

	    rs.close();

	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
	return studies.toArray(new Study[studies.size()]);
    }

    public ArrayList<Study> queryStudies(QueryStudy request) throws DataException {

	if (request == null)
	    throw new IllegalArgumentException("Request could not be null.");

	// проверяем валидность направления
	request.chechEntity();

	PreparedStatement psSelect = null;

	// TODO Сделать ограничение на количество возвращаемых строк

	// "SELECT * FROM WEBDICOM.STUDY"
	// + " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
	// + " order by PATIENT_NAME, STUDY_DATE "

	String sqlAddon = "";

	if (request.getId() != null) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " ID = ? ";
	}

	if (request.getStudyId() != null && request.getStudyId().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " STUDY_ID = ? ";
	}

	if (request.getStudyModality() != null && request.getStudyModality().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " STUDY_MODALITY = ? ";
	}

	if (request.getManufacturerModelName() != null && request.getManufacturerModelName().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " STUDY_MANUFACTURER_MODEL_NAME = ? ";
	}

	if (request.getPatientId() != null && request.getPatientId().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " UPPER(PATIENT_ID) like UPPER( ? || '%') ";
	}

	if (request.getPatientName() != null && request.getPatientName().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " UPPER(PATIENT_NAME) like UPPER( ? || '%') ";
	}

	if (request.getPatientShortName() != null && request.getPatientShortName().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " PATIENT_SHORTNAME = UPPER(?) ";
	}

	if (request.getPatientBirthDate() != null && request.getPatientBirthDate().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " PATIENT_BIRTH_DATE = ? ";
	}

	if (request.getPatientSex() != null && request.getPatientSex().length() > 0) {
	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " PATIENT_SEX = ? ";
	}

	if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
		&& request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0) {

	    // TODO Вынести в chekEntity
	    if (java.sql.Date.valueOf(request.getBeginStudyDate()).getTime() > java.sql.Date.valueOf(
		    request.getEndStudyDate()).getTime()) {
		throw new DataException(new IllegalArgumentException("beginStudyDate > endStudyDate"));
	    }

	    if (sqlAddon.length() != 0)
		sqlAddon += " AND ";
	    sqlAddon += " STUDY_DATE BETWEEN ? AND ? ";
	} else {

	    if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
		    && (request.getEndStudyDate() == null || request.getEndStudyDate().length() > 0)) {
		if (sqlAddon.length() != 0)
		    sqlAddon += " AND ";
		sqlAddon += " STUDY_DATE > ? ";
	    }

	    if (request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0
		    && (request.getBeginStudyDate() == null || request.getBeginStudyDate().length() > 0)) {
		if (sqlAddon.length() != 0)
		    sqlAddon += " AND ";
		sqlAddon += " STUDY_DATE < ?  ";
	    }

	}

	// TODO странная конструкция. пересмотреть..сделать isOld или isNew...
	if (request.getStudyResult() != null && request.getStudyResult().length() > 0) {
	    if (sqlAddon.length() != 0)

		if (request.getStudyResult().equals("new")) {
		    sqlAddon += " AND ";
		    sqlAddon += " ( STUDY_VIEW_PROTOCOL is NULL OR STUDY_VIEW_PROTOCOL = '' ) ";
		}
	    if (request.getStudyResult().equals("old")) {
		sqlAddon += " AND ";
		sqlAddon += " ( STUDY_VIEW_PROTOCOL IS NOT NULL AND STUDY_VIEW_PROTOCOL != '' )";
	    }

	}
	String order = "PATIENT_NAME, STUDY_DATE";
	if (request.getSortOrder() != null) {
	    order = request.getSortOrder();
	}

	String sql = "SELECT * FROM WEBDICOM.STUDY" + " WHERE " + sqlAddon + " order by " + order;

	IllegalArgumentException ex = new IllegalArgumentException("All query arguments empty! Set any argument's");
	if (sqlAddon.length() == 0)
	    throw new DataException(ex);

	try {

	    psSelect = connection.prepareStatement(sql);
	    int index = 1;

	    if (request.getId() != null) {
		psSelect.setLong(index++, request.getId());
	    }

	    if (request.getStudyId() != null && request.getStudyId().length() > 0) {
		psSelect.setString(index++, request.getStudyId());
	    }

	    if (request.getStudyModality() != null && request.getStudyModality().length() > 0) {
		psSelect.setString(index++, request.getStudyModality());
	    }

	    if (request.getManufacturerModelName() != null && request.getManufacturerModelName().length() > 0) {
		psSelect.setString(index++, request.getManufacturerModelName());
	    }

	    if (request.getPatientId() != null && request.getPatientId().length() > 0) {
		psSelect.setString(index++, request.getPatientId());
	    }

	    if (request.getPatientName() != null && request.getPatientName().length() > 0) {
		psSelect.setString(index++, request.getPatientName());
	    }

	    if (request.getPatientShortName() != null && request.getPatientShortName().length() > 0) {
		psSelect.setString(index++, request.getPatientShortName());
	    }

	    if (request.getPatientBirthDate() != null && request.getPatientBirthDate().length() > 0) {
		psSelect.setDate(index++, java.sql.Date.valueOf(request.getPatientBirthDate()));
	    }

	    if (request.getPatientSex() != null && request.getPatientSex().length() > 0) {
		psSelect.setString(index++, request.getPatientSex());
	    }

	    if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
		    && request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0) {
		psSelect.setDate(index++, java.sql.Date.valueOf(request.getBeginStudyDate()));
		psSelect.setDate(index++, java.sql.Date.valueOf(request.getEndStudyDate()));
	    } else {

		if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
			&& (request.getEndStudyDate() == null || request.getEndStudyDate().length() > 0)) {
		    psSelect.setDate(index++, java.sql.Date.valueOf(request.getBeginStudyDate()));
		}

		if (request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0
			&& (request.getBeginStudyDate() == null || request.getBeginStudyDate().length() > 0)) {
		    psSelect.setDate(index++, java.sql.Date.valueOf(request.getEndStudyDate()));
		}
	    }

	    ResultSet rs = psSelect.executeQuery();

	    ArrayList<Study> data = new ArrayList<Study>();

	    int counter = 1;
	    while (rs.next()) {
		// TODO убрать в конфиг
		if (counter++ > WorkListPanel.maxResultCount)
		    break;
		data.add(getStudyFromRs(rs));
	    }
	    rs.close();

	    return data;

	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
    }

    /**
     * Установка/удаление флага "удален" для исследования
     * 
     * @param connection
     * @param idStudy
     * @param removed
     * @throws DataException
     */
    public static void studyRemoveRestore(Connection connection, long idStudy, boolean removed) throws DataException {

	PreparedStatement psSelect = null;
	String sql = null;
	if (removed)
	    sql = "UPDATE WEBDICOM.STUDY SET REMOVED = CURRENT_TIMESTAMP WHERE ID = ?";
	else
	    sql = "UPDATE WEBDICOM.STUDY SET REMOVED = NULL WHERE ID = ?";

	try {

	    psSelect = connection.prepareStatement(sql);
	    psSelect.setLong(1, idStudy);
	    connection.setAutoCommit(false);
	    int count = psSelect.executeUpdate();
	    connection.commit();

	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
    }

    /**
     * Установка/удаление флага "удален" для файла с исследованием
     * 
     * @param connection
     * @param idDcmFile
     * @param removed
     * @throws DataException
     */
    public static void dcmFileRemoveRestore(Connection connection, long idDcmFile, boolean removed)
	    throws DataException {

	PreparedStatement psSelect = null;
	String sql = null;
	if (removed)
	    sql = "UPDATE WEBDICOM.DCMFILE SET REMOVED = CURRENT_TIMESTAMP WHERE ID = ?";
	else
	    sql = "UPDATE WEBDICOM.DCMFILE SET REMOVED = NULL WHERE ID = ?";

	try {

	    psSelect = connection.prepareStatement(sql);
	    psSelect.setLong(1, idDcmFile);
	    connection.setAutoCommit(false);
	    int count = psSelect.executeUpdate();
	    connection.commit();

	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}
    }

    /**
     * Получение прокси-класса для файла исследования TODO Переделать на
     * сущность DcmFile (перенести в пакет ORM)
     * 
     * @param connection
     * @param idFile
     * @return
     * @throws SQLException
     */
    public static ArrayList<DcmFileProxy> getDcmFileProxies(Connection connection, long idStudy) throws DataException {

	PreparedStatement psFiles = null;

	try {
	    psFiles = connection.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");
	    psFiles.setLong(1, idStudy);
	    ResultSet rs = psFiles.executeQuery();

	    ArrayList<DcmFileProxy> files = new ArrayList<DcmFileProxy>();
	    while (rs.next()) {
		DcmFileProxy dcmfileProxy = new DcmFileProxy();

		dcmfileProxy.setId(rs.getLong("ID"));
		dcmfileProxy.setIdStudy(rs.getLong("FID_STUDY"));
		dcmfileProxy.setType(rs.getString("TYPE"));
		dcmfileProxy.setMimeType(rs.getString("MIME_TYPE"));
		dcmfileProxy.setEncapsulatedDocSize(rs.getLong("DOCUMENT_SIZE"));
		dcmfileProxy.setFileName(rs.getString("DCM_FILE_NAME"));
		dcmfileProxy.setFileSize(rs.getLong("DCM_FILE_SIZE"));
		dcmfileProxy.setImageSize(rs.getLong("IMAGE_FILE_SIZE"));
		dcmfileProxy.setImageWidth(rs.getInt("IMAGE_WIDTH"));
		dcmfileProxy.setImageHeight(rs.getInt("IMAGE_HEIGHT"));

		dcmfileProxy.setDateRemoved(null);
		if (rs.getTimestamp("REMOVED") != null)
		    dcmfileProxy.setDateRemoved(rs.getTimestamp("REMOVED"));

		files.add(dcmfileProxy);

	    }
	    return files;
	} catch (SQLException e) {
	    throw new DataException(e);
	} finally {

	    try {
		if (psFiles != null)
		    psFiles.close();

	    } catch (SQLException e) {
		throw new DataException(e);
	    }
	}

    }
}
