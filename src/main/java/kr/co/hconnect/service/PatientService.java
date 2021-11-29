package kr.co.hconnect.service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.hconnect.common.CryptoUtils;
import kr.co.hconnect.domain.LoginInfo;
import kr.co.hconnect.domain.Patient;
import kr.co.hconnect.domain.SearchExistLoginInfo;
import kr.co.hconnect.domain.SearchLoginIdInfo;
import kr.co.hconnect.exception.NotFoundPatientInfoException;
import kr.co.hconnect.exception.NotMatchPatientPasswordException;
import kr.co.hconnect.repository.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 환자관리 Service
 */
@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class PatientService extends EgovAbstractServiceImpl {

    /**
     * 환자정보 관리 Dao
     */
    private final PatientDao patientDao;

    @Autowired
    public PatientService (PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    /**
     * 환자정보 조회-로그인
     * 
     * @param loginInfo 로그인 정보
     * @return 환자정보
     * @throws NotFoundPatientInfoException 환자정보가 없을 경우 exception
     * @throws NotMatchPatientPasswordException 비밀번호가 일치하지 않을 경우 exception
     */
    public Patient selectPatientByLoginInfo(LoginInfo loginInfo)
            throws NotFoundPatientInfoException, NotMatchPatientPasswordException {
        // 환자정보 조회
        Patient patient = patientDao.selectPatientByLoginId(loginInfo.getLoginId());

        // 전달받은 비밀번호 암호화
        String encryptPassword = CryptoUtils.encrypt(loginInfo.getPassword());

        if (patient == null) {
            throw new NotFoundPatientInfoException(String.format("[%s] 로그인ID의 환자정보가 존재하지 않습니다.", loginInfo.getLoginId()));
        } else if (!patient.getPassword().equals(encryptPassword)) {
            throw new NotMatchPatientPasswordException("");
        }

        return patient;
    }

    /**
     * 환자 로그인ID 중복 체크
     *
     * @param loginId 로그인ID
     * @return boolean 중복여부
     */
    public boolean checkDuplicateLoginId(String loginId) {
        boolean isDuplicate = false;
        // 환자정보 조회
        Patient patient = patientDao.selectPatientByLoginId(loginId);

        if (patient != null) {
            isDuplicate = true;
        }

        return isDuplicate;
    }

    /**
     * 환자정보 저장 - App을 통한 환자정보 추가
     *
     * @param patient 환정보
     * @return Patient 환자정보
     */
    public Patient savePatientInfo(Patient patient) {

        // 환자정보 신규생성
        if (patient.getFlag().equals("A")) {
            // 주민번호 기준 환저정보 존재여부 확인
            Patient patientBySsn = patientDao.selectPatientBySsn(CryptoUtils.encrypt(patient.getSsn()));

            if (patientBySsn == null) {
                throw new NotFoundPatientInfoException("해당 주민번호로 생성된 환자정보가 존재하지 않습니다.");
            }

            patient.setPatientId(patientBySsn.getPatientId());
        }

        // 비밀번호 암호화
        patient.setPassword(CryptoUtils.encrypt(patient.getPassword()));

        // 환자정보 업데이트
        int affectedRow = patientDao.updatePatientInfo(patient);

        return patientDao.selectPatientByLoginId(patient.getLoginId());
    }

    /**
     * 환자정보 조회-아이디 검색 조건 정보 기준
     * 
     * @param searchLoginIdInfo 아이디 검색 조건 정보
     * @return List&ltPatient&gt
     */
    public List<Patient> selectPatientBySearchLoginIdInfo(SearchLoginIdInfo searchLoginIdInfo) {
        return patientDao.selectPatientListBySearchLoginIdInfo(searchLoginIdInfo);
    }

    /**
     * 환자정보 조회-개인정보 확인 검색 조건 기준
     *
     * @param searchExistLoginInfo 개인정보 확인 검색 조건
     * @return List&ltPatient&gt
     */
    public List<Patient> selectPatientBySearchExistLoginInfo(SearchExistLoginInfo searchExistLoginInfo) {
        return patientDao.selectPatientBySearchExistLoginInfo(searchExistLoginInfo);
    }
}
