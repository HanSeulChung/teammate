import {EventInput, EventForm} from '../../styles/CreateEventStyled'
import { CommonSubmitBtn } from '../../styles/CommonStyled';
import { useState, useEffect } from 'react';
import axios from "axios";

const EditEvent = ({isEdit, originEvent, setEventList, toggleIsEdit}) => {
    // input값 담아둘 state
    const [eventChange, setEventChange] = useState({
        id: originEvent.id,
        title: "",
        start: originEvent.start,
        contents: "", 
        place: "", 
        groupId: "주간회의"
    })

    // input값 전송 직전 state
    const [newEvent, setNewEvent] = useState({
        id: originEvent.id,
        title: "",
        start: originEvent.start,
        extendedProps: {
            contents: "", 
            place: "", 
            groupId: ""
        }
    })

    const handleEventChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEventChange((prev) => ({...prev, [e.target.name] : e.target.value}));
    }

    // 입력값 추적
    useEffect(() => {
        setNewEvent({
            id: originEvent.id,
            title: eventChange.title,
            start: eventChange.start,
            extendedProps: {
                contents: eventChange.contents, 
                place: eventChange.place, 
                groupId: eventChange.groupId
            }
        })
    }, [eventChange]);

    // 수정중 토글 여부
    useEffect(() => {
        if(isEdit) {
            setEventChange(originEvent);
        }
    },[isEdit])
    
    // 새 일정 등록 요청
    const handleScheduleSubmit = async (e) => {
        e.preventDefault();
        // console.log("입력제목값000000000000 => "+eventChange.title);
        // console.log("0000000000000000"+JSON.stringify(newEvent));
        setNewEvent({
            id: "",
            title: eventChange.title,
            start: eventChange.start,
            extendedProps: { 
                contents: eventChange.contents, 
                place: eventChange.place,
                groupId: eventChange.groupId,
            }
        });
        // console.log("111111111111111111111111111"+JSON.stringify(newEvent));
        try {
            const res = await axios.post("/schedules", newEvent, {
                headers:{
                    "Content-Type": "application/json"
                },
            });
            if (res.status === 201) {
                setEventChange({
                    id: "",
                    title: "",
                    start: new Date(),
                    contents: "", 
                    place: "", 
                    groupId: ""
                });
                console.log(res.data);
                setEventList(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    };

    // 일정 수정 요청
    const handleScheduleModify = async (e) => {
        e.preventDefault();
        setNewEvent({
            id: originEvent.id,
            title: eventChange.title,
            start: eventChange.start,
            extendedProps: { 
                contents: eventChange.contents, 
                place: eventChange.place,
                groupId: eventChange.groupId,
            }
        });
        try {
            const res = await axios.put("/schedules", newEvent, {
                headers:{
                    "Content-Type": "application/json"
                },
            });
            if (res.status === 201) {
                setEventChange({
                    id: res.data.id,
                    title: res.data.title,
                    start: res.data.start,
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
            ): (
                <h2>새 일정 등록</h2>
            )}
            <label htmlFor="start">시작시간 끝시간</label>
            <EventInput 
                type="datetime-local" 
                name="start" 
                id="start"
                value={eventChange.start} 
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

            <label htmlFor="contents">일정내용</label>
            <EventInput 
                type="text" 
                name="contents" 
                id="contents" 
                value={eventChange.contents} 
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
            
            <label htmlFor="category" value={eventChange.groupId} onChange={handleEventChange}>카테고리</label>
            <select id="category">
                <option value="1">주간회의</option>
                <option value="2">회의</option>
                <option value="3">미팅</option>
            </select>
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