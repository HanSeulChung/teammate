import { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import axiosInstance from "../../axios";
import styled from "styled-components";
import Select, { MultiValue } from 'react-select';
// import SelectTeamMember from "./SelectTeamMember.tsx";

type AddEventProps = {
  originEvent: any,
  setEventList: React.Dispatch<React.SetStateAction<any>>,
  myTeamMemberId: number,
}

const AddEvent = ({ originEvent, setEventList, myTeamMemberId }: AddEventProps) => {
  // 현재 페이지의 팀 아이디
  const { teamId } = useParams();

  // 팀원 목록 값
  const [teamParticipants, setTeamParticipants] = useState<any[]>([]);

  interface ITeamMemberList {
    value: number;
    label: string;
  }

  // 팀원 닉네임과 아이디만 가져오기
  const teamMemberList = (response: any[]): ITeamMemberList[] => {
    return response.map(res => ({
      value: res.teamParticipantsId,
      label: res.teamNickName,
    }))
  }

  // 해당 페이지의 팀원목록 가져오기
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

  useEffect(() => {
    fetchParticipants();
  }, [teamId]);

  // input값 담아둘 state
  const [eventChange, setEventChange] = useState({
    teamId: teamId,
    categoryId: 1,
    title: "",
    content: "",
    place: "",
    startDt: originEvent.start,
    endDt: originEvent.end,
    repeatCycle: null,
    color: "#ff0000",
    createParticipantId: myTeamMemberId,
    teamParticipantsIds: [] as any,
  })

  const [participantsIds, setParticipantsIds] = useState<number[]>([]);

  // 참여자 외의 정보 입력값 핸들링
  const handleEventChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setEventChange((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  }

  // 참여자 정보 입력값 핸들링
  const handleEventMemberChange = (newValue: MultiValue<any>) => {
    let result: any = [];
    newValue.forEach(item => {
      const member = teamParticipants.find(f => f.value == item.value);
      result.push(member);
    });
    const valuesArray: number[] = result.map((option: any) => option.value);

    setEventChange((prev) => ({ ...prev, teamParticipantsIds: result as any }));
    setParticipantsIds(valuesArray);
  };

  // 새 일정 등록 요청
  const handleScheduleSubmit = async (e: any) => {
    e.preventDefault();
    // console.log("입력제목값000000000000 => "+eventChange.title);
    // console.log("0000000000000000"+JSON.stringify(newEvent));
    // console.log("111111111111111111111111111"+JSON.stringify(newEvent));

    console.log(eventChange);
    console.log(participantsIds);

    let result = eventChange;
    result.teamParticipantsIds = participantsIds;

    try {
      const res = await axiosInstance.post(`/team/${teamId}/schedules`, eventChange, {
        headers: {
          "Content-Type": "application/json"
        },
      });
      if (res.status === 201) {
        console.log(res.data);
        setEventList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <form className="p-4 md:p-5">
      <h2 className="text-lg font-semibold text-gray-900">새 일정 등록</h2>
      <div>
        <label htmlFor="startDt" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>시작시간 - 끝시간</label>
        <input
          type="datetime-local"
          name="startDt"
          id="startDt"
          value={eventChange.startDt}
          onChange={handleEventChange}
          className='inline bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 w-30 p-2.5'
        />
        <input
          type="datetime-local"
          name="endDt"
          id="endDt"
          value={eventChange.endDt}
          onChange={handleEventChange}
          className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 w-30 p-2.5'
        />
      </div>
      <div className="col-span-2">
        <label htmlFor="title" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>일정제목</label>
        <EventInput
          type="text"
          name="title"
          id="title"
          value={eventChange.title}
          onChange={handleEventChange}
          className='block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
        />
      </div>
      <div className="col-span-2">
        <label htmlFor="content" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>일정내용</label>
        <EventInput
          type="text"
          name="content"
          id="content"
          value={eventChange.content}
          onChange={handleEventChange}
          className='block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
        />
      </div>
      <div className="col-span-2">
        <label htmlFor="place" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>일정장소</label>
        <EventInput
          type="text"
          name="place"
          id="place"
          value={eventChange.place}
          onChange={handleEventChange}
          className='block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
        />
      </div>
      <div className='col-span-2'>
        <label htmlFor="repetition" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>반복</label>
        <select id="repetition" className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5'>
          <option value="NULL">반복 안함</option>
          <option value="WEEKLY">매주</option>
          <option value="MONTHLY">매달</option>
        </select>
      </div>
      <div className='col-span-2'>
        <label htmlFor="categoryId" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>카테고리</label>
        <select id="categoryId" name='categoryId' value={eventChange.categoryId} onChange={handleEventChange} className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5'>
          <option value="1">주간회의</option>
          <option value="2">회의</option>
          <option value="3">미팅</option>
        </select>
      </div>
      <div className='col-span-2'>
        <label htmlFor="color" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>색상</label>
        <select id="color" name="color" value={eventChange.color} onChange={handleEventChange} className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5'>
          <option value="#7aac7a">초록</option>
          <option value="#E21D29">빨강</option>
          <option value="#336699">파랑</option>
        </select>
      </div>
      <div className='col-span-2'>
        <label htmlFor="teamParticipantsIds" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>참여자</label>
        <Select
          defaultValue={[teamParticipants[0]]}
          isMulti
          name="teamParticipantsIds"
          classNamePrefix="select"
          id="teamParticipantsIds"
          options={teamParticipants}
          value={eventChange.teamParticipantsIds}
          onChange={(newValue: MultiValue<any>) => {
            handleEventMemberChange(newValue);
            // console.log("뉴 밸류 -> ",newValue);
            // console.log("뉴 밸류 에서 접근 -> ",newValue[0]);
            // console.log("뉴 밸류 에서 접근접근 -> ",newValue[0]['value']);
            // console.log("이벤트체인지 -> ",eventChange);
          }}
          className='basic-multi-select bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5 mb-4'
        />
      </div>
      <CommonSubmitBtn
        onClick={(e: any) => {
          //handleMemberIds();
          handleScheduleSubmit(e);
        }}
        className='mt-2'
      >등록</CommonSubmitBtn>
    </form>
  );
};

export default AddEvent;

// 스타일드 컴포넌트
export const EventForm = styled.form`
  display: flex;
  flex-flow: column; 
`

export const EventInput = styled.input`
  padding: 3px 0px 3px 0px;
`
export const CommonSubmitBtn = styled.button`
  background-color: #A3CCA3;
`