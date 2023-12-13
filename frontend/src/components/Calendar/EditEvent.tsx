import {EventInput, EventForm} from '../../styles/CreateEventStyled'
import { CommonSubmitBtn } from '../../styles/CommonStyled';
import { useState, useEffect } from 'react';
import axios from "axios";

// import { schedules } from "../../recoil/atoms/schedules.tsx"
// import { useRecoilState } from 'recoil';

const EditEvent = ({isEdit, originEvent}) => {
    // 실제 등록할 state 값
    // const [newSchedule, setNewSchedule] = useRecoilState(schedules)

    // input값 담아둘 state
    const [eventChange, setEventChange] = useState({
        title: "",
        start: new Date(),
        contents: "", 
        place: "", 
        groupId: ""
    })

    // input값 전송 직전 state
    const [newEvent, setNewEvent] = useState({
        title: "",
        start: new Date(),
        extendedProps: {
            contents: "", 
            place: "", 
            groupId: ""
        }
    })

    const handleEventChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setEventChange((prev) => ({...prev, [e.target.name] : e.target.value}));
    }

    // 입력값 추적
    useEffect(() => {
        // console.log("useEffect 입력값 추적 : ", eventChange);
        setNewEvent({
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
    
    // // 전송 전 setData 추적
    // useEffect(() => {
    //     console.log("useEffect 전송값 추적 : ", newEvent);
    // }, [newEvent]);
    

    const handleScheduleSubmit = async (event) => {
        event.preventDefault();
        console.log("입력제목값000000000000 => "+eventChange.title);
        console.log("0000000000000000"+JSON.stringify(newEvent));
        setNewEvent({
            title: eventChange.title,
            start: eventChange.start,
            extendedProps: { 
                contents: eventChange.contents, 
                place: eventChange.place,
                groupId: eventChange.groupId,
            }
        });
        console.log("111111111111111111111111111"+JSON.stringify(newEvent));
        try {
            const res = await axios.post("/schedules", newEvent, {
                headers:{
                    "Content-Type": "application/json"
                },
            });
            if (res.status === 201) {
                setEventChange({
                    title: "",
                    start: new Date(),
                    contents: "", 
                    place: "", 
                    groupId: ""
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
                <option value="first">반복 안함</option>
                <option value="second">매일</option>
                <option value="third">매주</option>
            </select>
            
            <label htmlFor="category">카테고리</label>
            <select id="category">
                <option value="first">주간회의</option>
                <option value="second">회의</option>
                <option value="third">미팅</option>
            </select>
            {isEdit ? (
                <>
                    <button>그냥버튼1</button>
                    <button>그냥버튼2</button>
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