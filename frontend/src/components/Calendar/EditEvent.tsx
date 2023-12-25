import { EventInput, EventForm } from '../../styles/CreateEventStyled'
import { CommonSubmitBtn } from '../../styles/CommonStyled';
import { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import axiosInstance from "../../axios";

type EditEventProps = {
  isEdit: boolean,
  originEvent: any,
  setEventList: React.Dispatch<React.SetStateAction<any>>,
  toggleIsEdit: () => void,
}

const EditEvent = ({ isEdit, originEvent, setEventList, toggleIsEdit }: EditEventProps) => {
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
  useEffect(() => {
    if (isEdit) {
      setEventChange(originEvent);
    }
  }, [isEdit])

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
  const handleScheduleModify = async (e: any) => {
    e.preventDefault();
    try {
      const res = await axiosInstance.put("/schedules", newEvent, {
        headers: {
          "Content-Type": "application/json"
        },
      });
      if (res.status === 201) {
        setEventChange({
          id: res.data.id,
          title: res.data.title,
          start: res.data.start,
          end: res.data.end,
          contents: res.data.contents,
          place: res.data.place,
          groupId: res.data.groupId,
        });
        // setNewEvent()
        console.log(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <EventForm>
      {isEdit ? (
        <h2>일정 수정</h2>
      ) : (
        <h2>새 일정 등록</h2>
      )}
      <label htmlFor="startDt">시작시간 끝시간</label>
      <EventInput
        type="datetime-local"
        name="startDt"
        id="startDt"
        value={eventChange.startDt}
        onChange={handleEventChange}
      />
      <EventInput
        type="datetime-local"
        name="endDt"
        id="endDt"
        value={eventChange.endDt}
        onChange={handleEventChange}
      />
      <label htmlFor="title">일정제목</label>
      <EventInput
        type="text"
        name="title"
        id="title"
        value={eventChange.title}
        onChange={handleEventChange}
      />
      <label htmlFor="content">일정내용</label>
      <EventInput
        type="text"
        name="content"
        id="content"
        value={eventChange.content}
        onChange={handleEventChange}
      />
      <label htmlFor="place">일정장소</label>
      <EventInput
        type="text"
        name="place"
        id="place"
        value={eventChange.place}
        onChange={handleEventChange}
      />
      <label htmlFor="repetition">반복</label>
      <select id="repetition">
        <option value="Does not repeat">반복 안함</option>
        <option value="Daily">매일</option>
        <option value="Weekly">매주</option>
      </select>
      <label htmlFor="categoryId">카테고리</label>
      <select id="categoryId" value={eventChange.categoryId} onChange={handleEventChange}>
        <option value="1">주간회의</option>
        <option value="2">회의</option>
        <option value="3">미팅</option>
      </select>
      <label htmlFor="color">색상</label>
      <select id="color" value={eventChange.color} onChange={handleEventChange}>
        <option value="#ff0000">빨강</option>
        <option value="#ff0000">노랑</option>
        <option value="#ff0000">초록</option>
      </select>
      <label htmlFor="teamParticipantsIds">참여자</label>
      <input id="teamParticipantsIds" value={eventChange.teamParticipantsIds} onChange={handleEventChange}></input>
      {isEdit ? (
        <>
          <button onClick={handleScheduleModify}>수정완료</button>
          <button onClick={toggleIsEdit}>취소</button>
        </>
      ) : (
        <CommonSubmitBtn
          onClick={handleScheduleSubmit}
        >등록</CommonSubmitBtn>
      )}
    </EventForm>
  );
};

export default EditEvent;