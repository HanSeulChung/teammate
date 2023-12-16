// useTeamCreation.ts
import { useRecoilState } from "recoil";
import { userTeamsState, Team } from "../../state/authState";

export const useTeamCreation = () => {
  const [userTeams, setUserTeams] = useRecoilState(userTeamsState);

  const handleTeamCreation = (newTeam: Team) => {
    const updatedUserTeams = [...userTeams, newTeam];
    setUserTeams(updatedUserTeams);
  };

  return handleTeamCreation;
};
