import Select from 'react-select';
import { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import axiosInstance from "../../axios";

const SelectTeamMember = () => {
  // 팀 아이디
  const { teamId } = useParams();

  // 팀원 목록
  const  [teamParticipants, setTeamParticipants] = useState<any[]>([]);

  // 팀원 닉네임과 아이디
  interface ITeamMemberList {
    value: string;
    label: string;
  }

  const teamMemberList = (response: any[]): ITeamMemberList[] => {
    return response.map(res => ({
      value: res.teamParticipantsId,
      label: res.teamNickName,
    }))
  }

  const handleMemberIds = () => {
    // teamParticipantsIds 배열에서 value 값만 추출하여 새로운 배열 생성
    const valuesArray: number[] = teamParticipants.map((option) => option.value);
    console.log('Values Array:', valuesArray);
  };

  useEffect(() => {
    const fetchParticipants = async () => {
      try {
        const response = await axiosInstance.get(`/team/${teamId}/participant/list`, {});
        console.log("임포트 셀렉트 컴포넌트 -> ", response);
        
        const memberList = teamMemberList(response.data);
        
        setTeamParticipants(memberList);
        console.log("임포트 셀렉트 컴포넌트 스테이트 -> ", teamParticipants);
        
      } catch (error) {
        console.error("Error fetching participants:", error);
      }
    };
    fetchParticipants();
  }, [teamId]);

  return (
    <div>
      <Select
        defaultValue={[teamParticipants[0]]}
        isMulti
        name="colors"
        options={teamParticipants}
        className="basic-multi-select"
        classNamePrefix="select"
      />
      <button onClick={handleMemberIds}>Click Me</button>
    </div>
  )
}

export default SelectTeamMember;