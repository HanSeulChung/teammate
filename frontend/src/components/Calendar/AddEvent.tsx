import { useState } from 'react';
import { useParams } from "react-router-dom";
import axiosInstance from "../../axios";
import styled from "styled-components";

type AddEventProps = {
  originEvent: any,
  setEventList: React.Dispatch<React.SetStateAction<any>>,
}

const AddEvent = ({ originEvent, setEventList }: AddEventProps) => {
  // 현재 페이지의 팀 아이디
  const { teamId } = useParams();

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
    teamParticipantsIds: [],
  })

  const handleEventChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setEventChange((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  }

  // 수정중 토글 여부
  // useEffect(() => {
  //   if (isEdit) {
  //     setEventChange(originEvent);
  //   }
  // }, [isEdit])

  // 새 일정 등록 요청
  const handleScheduleSubmit = async (e: any) => {
    e.preventDefault();
    // console.log("입력제목값000000000000 => "+eventChange.title);
    // console.log("0000000000000000"+JSON.stringify(newEvent));
    // console.log("111111111111111111111111111"+JSON.stringify(newEvent));
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

  // 일정 수정 요청
  // const handleScheduleModify = async (e: any) => {
  //   e.preventDefault();
  //   try {
  //     const res = await axiosInstance.put("/schedules", newEvent, {
  //       headers: {
  //         "Content-Type": "application/json"
  //       },
  //     });
  //     if (res.status === 201) {
  //       setEventChange({
  //         id: res.data.id,
  //         title: res.data.title,
  //         start: res.data.start,
  //         end: res.data.end,
  //         contents: res.data.contents,
  //         place: res.data.place,
  //         groupId: res.data.groupId,
  //       });
  //       // setNewEvent()
  //       console.log(res.data);
  //     }
  //   } catch (error) {
  //     console.log(error);
  //   }
  // };

  return (
    // <EventForm>
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
          <option value="Does not repeat">반복 안함</option>
          <option value="Daily">매일</option>
          <option value="Weekly">매주</option>
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
          <option value="#ff0000">빨강</option>
          <option value="#ff0000">노랑</option>
          <option value="#ff0000">초록</option>
        </select>
      </div>

      <div className='col-span-2'>
        <label htmlFor="teamParticipantsIds" className='block mt-2 mb-2 text-sm font-medium text-gray-900'>참여자</label>
        <input id="teamParticipantsIds" value={eventChange.teamParticipantsIds} onChange={handleEventChange} className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5 mb-4'></input>
      </div>
        <CommonSubmitBtn
          onClick={handleScheduleSubmit}
          className='mt-2'
        >등록</CommonSubmitBtn>
    </form>
    // </EventForm>
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