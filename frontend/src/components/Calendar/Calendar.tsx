import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { ICategoryList } from "../../interface/interface.ts"
import axiosInstance from "../../axios";
import getCategoryList from "./GetCategoryList.tsx";
import TeamCalender from "./TeamCalender"
import CalendarCategory from "./CalendarCategory"

const Calendar = () => {
  // 팀 아이디
  const { teamId } = useParams();

  // 카테고리 목록
  const [categoryList, setCategoryList] = useState<ICategoryList[]>([
    {
      categoryId: 1,
      categoryName: "카테고리1",
    },
  ]);

  // 현재 페이지의 사용자 팀 멤버 Id(participant)
  const [myTeamMemberId, setMyTeamMemberId] = useState<number | undefined>(undefined);

  const fetchMyTeamMemberId = async () => {
    try {
      // 사용자가 속해있는 팀 목록과 닉네임등의 정보를 불러옴
      const response = await axiosInstance.get("/member/participants", {});
      // 사용자가 가입한 팀 목록중에 현재 팀id의 정보와 맞는 팀 내 내정보 값만 가져옴
      const myTeamMemberInfo = response.data.find(
        (item: { teamId: number }) => item.teamId === Number(teamId),
      );
      const authorTeamMemberId = myTeamMemberInfo.teamParticipantsId;      
      setMyTeamMemberId(authorTeamMemberId);
      // console.log("작성자 팀멤버 아이디 -> ",myTeamMemberId);
    } catch (error) {
      console.error("Error fetching participants:", error);
    }
  };

  useEffect(() => {
    const getCategoryItems = async() => {
      const response = await getCategoryList(teamId);
      setCategoryList(response);
    }

    fetchMyTeamMemberId();
    getCategoryItems();
  }, [teamId]);

  return (
    <div className="flex min-h-screen flex-col items-center justify-between">
      <div className="grid grid-cols-10">
        <div className="col-span-8">
          <TeamCalender categoryList={categoryList} myTeamMemberId={myTeamMemberId} />
        </div>
        <div className="ml-8 w-full mt-16 lg:h-1/2">
          <CalendarCategory categoryList={categoryList} myTeamMemberId={myTeamMemberId} setCategoryList={setCategoryList} />
        </div>
      </div>
    </div>
  )
}

export default Calendar;