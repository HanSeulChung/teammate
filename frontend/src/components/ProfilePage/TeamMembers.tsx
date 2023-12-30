import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import profileImg from "../../assets/profileImg.png";
import { Team, TeamParticipant } from "../../interface/interface";
import axiosInstance from "../../axios";
import {
  TeamProfileContainer,
  TeamProfileTitle,
  TeamInfoContainer,
  TeamImage,
  SelectContainer,
  SearchInput,
  MemberContainer,
  MemberInput,
  MoveTeamPage,
} from "./TeamMembersStyled";

const TeamMembers = () => {
  const [team, setTeam] = useState<Team | null>(null);
  const [searchTeam, setSearchTeam] = useState("");
  const [teamParticipants, setTeamParticipants] = useState<TeamParticipant[]>(
    [],
  );
  const { teamId } = useParams();
  const navigate = useNavigate();

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

  const navigateToTeamPage = () => {
    navigate(`/team/${teamId}`);
  };

  return (
    <div>
      <MoveTeamPage>
        <div onClick={navigateToTeamPage}>팀 페이지로 이동</div>
      </MoveTeamPage>
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
