package kr.co.hconnect.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import kr.co.hconnect.vo.AdmissionInfoVO;
import kr.co.hconnect.vo.AdmissionListVO;
import kr.co.hconnect.vo.AdmissionVO;

@Repository
public class AdmissionDao extends EgovAbstractMapper {

	/**
	 * 입소내역 리스트 조회
	 * @return List<AdmissionListVO>
	 */
	public List<AdmissionListVO> selectAdmissionList() {
		return selectList("kr.co.hconnect.sqlmapper.selectAdmissionList");
	}
	
	/**
	 * 입소내역 정보 조회
	 * @param vo AdmissionVO-입소내역VO
	 * @return affectedRow
	 */
	public AdmissionInfoVO selectAdmissionInfo(String admissionId) {
		return selectOne("kr.co.hconnect.sqlmapper.selectAdmissionInfo", admissionId);
	}
	
	/**
	 * 입소내역 생성
	 * @param vo AdmissionVO-입소내역VO
	 * @return affectedRow
	 */
	public int insertAdmission(AdmissionVO vo) {
		return insert("kr.co.hconnect.sqlmapper.insertAdmission", vo);
	}
	
	/**
	 * 입소내역 수정
	 * @param vo AdmissionVO-입소내역VO
	 * @return affectedRow
	 */
	public int updateAdmission(AdmissionVO vo) {
		return update("kr.co.hconnect.sqlmapper.updateAdmission", vo);
	}
	
	/**
	 * 퇴실처리
	 * @param vo AdmissionVO-입소내역VO
	 * @return affectedRow
	 */
	public int updateAdmissionDischarge(AdmissionVO vo) {
		return update("kr.co.hconnect.sqlmapper.updateAdmissionDischarge", vo);
	}
	
}
