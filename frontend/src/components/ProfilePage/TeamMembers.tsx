import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import styled from "styled-components";
import profileImg from "../../assets/profileImg.png";
import { Team, TeamParticipant } from "../../interface/interface";
import axiosInstance from "../../axios";

const TeamMembers = () => {
  const [team, setTeam] = useState<Team | null>(null);
  const [searchTeam, setSearchTeam] = useState("");
  const [teamParticipants, setTeamParticipants] = useState<TeamParticipant[]>(
    [],
  );
  const { teamId } = useParams();

  //팀 정보 가져오기
  useEffect(() => {
    const fetchTeamInfo = async () => {
      try {
        const response = await axiosInstance.get<Team>(`/team/${teamId}`);
        setTeam(response.data);
      } catch (error) {
        console.error("Error fetching team info:", error);
      }
    };
    const fetchTeamParticipants = async () => {
      try {
        const response = await axiosInstance.get<TeamParticipant[]>(
          `/team/${teamId}/participant/list`,
        );
        console.log(response.data);
        setTeamParticipants(response.data);
      } catch (error) {
        console.error("Error fetching team participants:", error);
      }
    };
    if (teamId) {
      fetchTeamInfo();
      fetchTeamParticipants();
    }
  }, [teamId]);

  // 팀원 목록
  const teamMembers = teamParticipants.map((participant) => ({
    id: participant.teamParticipantsId,
    name: participant.teamNickName,
    role: participant.teamRole,
  }));

  // 검색된 팀원 목록
  const filteredTeamMembers = teamMembers
    ? teamMembers
        .filter((member) =>
          member.name.toLowerCase().includes(searchTeam.toLowerCase()),
        )
        .sort((a, b) => {
          if (a.role === "LEADER" && b.role !== "LEADER") {
            return -1;
          } else if (a.role !== "LEADER" && b.role === "LEADER") {
            return 1;
          } else {
            return a.name.localeCompare(b.name);
          }
        })
    : [];
  // 팀장 여부에 따라 스타일을 적용하는 함수
  const getMemberStyle = (isTeamLeader: boolean) => {
    return {
      fontWeight: isTeamLeader ? "bold" : "normal",
    };
  };

  return (
    <div>
      <TeamProfileContainer>
        <TeamProfileTitle>{team?.name} 프로필</TeamProfileTitle>
        <TeamInfoContainer>
          {team ? (
            <>
              <TeamImage src={team.profileUrl || profileImg} alt="프로필" />
            </>
          ) : (
            <div>Loading...</div>
          )}
        </TeamInfoContainer>
        <SelectContainer>
          <SearchInput
            type="text"
            id="teamMemberSearch"
            placeholder="팀원 검색"
            value={searchTeam}
            onChange={(e) => setSearchTeam(e.target.value)}
          />
        </SelectContainer>

        {/* 팀원 목록 */}
        <SelectContainer>
          {filteredTeamMembers.length > 0 && (
            <MemberContainer>
              {filteredTeamMembers.map((member) => (
                <MemberInput
                  key={member.id}
                  type="text"
                  value={`${member.name} ${
                    member.role === "LEADER" ? "(팀장)" : ""
                  }`}
                  style={getMemberStyle(member.role === "LEADER")}
                />
              ))}
            </MemberContainer>
          )}
        </SelectContainer>
      </TeamProfileContainer>
    </div>
  );
};

export default TeamMembers;

const TeamProfileContainer = styled.div`
  text-align: center;
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
`;
const TeamProfileTitle = styled.h2`
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold; // 팀 이름 bold 스타일 적용
  font-size: 1.5em;
`;

const TeamInfoContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
`;

const TeamImage = styled.img`
  max-width: 150px;
  max-height: 150px;
  border-radius: 50%;
`;

const SelectContainer = styled.div`
  text-align: center;
  margin-top: 10px;
`;

const SearchInput = styled.input`
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  margin: 20px 0 10px 0;
  background: white;
  width: 100%;
`;

const MemberContainer = styled.div`
  max-height: 150px;
  overflow-y: auto;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 5px;
  display: flex;
  flex-direction: column;
`;

const MemberInput = styled.input`
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 5px;
  border: none;
  padding: 5px;
  background-color: transparent;
  cursor: pointer;
  &:hover {
    background-color: #f0f0f0;
  }
`;
