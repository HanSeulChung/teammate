import { useState, useEffect } from "react";
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import '../../styles/teamCalender.css'
import { Modal, Overlay, ModalContent, CloseModal, CalendarDiv } from '../../styles/TeamCalenderStyled.tsx'
import EditEvent from "./EditEvent.tsx";
import axios from "axios";

const TeamCalender = () => {
    // 모달팝업 유무 값
    const [eventDetailModal, setEventDetailModal] = useState(false);
    const [eventFormModal, setEventFormModal] = useState(false);
    
    // 일정 전체 목록
    const [eventList, setEventList] = useState([]);

    // 달력 일정 각각 state 핸들링용
    const [event, setEvent] = useState([]);

    // 모달팝업 관리
    const toggleModal = () => {
        setEventDetailModal(!eventDetailModal);
    };
    const toggleFormModal = () => {
        setEventFormModal(!eventFormModal);
    };

    // 일정클릭 핸들링
    const HandleEventClick = (e) => {
        // console.log(e.event.extendedProps);
        // console.log(e.event.start);
        setEvent({
            id: e.event.id,
            title: e.event.title,
            start: e.event.start,
            contents: e.event.extendedProps.contents,
            place: e.event.extendedProps.place,
            groupId: e.event.extendedProps.groupId,
        });
        toggleModal();
    }

    // 날짜클릭 핸들링
    const HandleDateClick = (e) => {
        // console.log(e.dayEl);
        toggleFormModal();
    }

    // 수정중인지 여부
    const [isEdit, setIsEdit] = useState(false);
    const toggleIsEdit = () => setIsEdit(!isEdit);

    // 일정목록 불러오기
    const getAllEvents = async () => {
        try {
            const res = await axios({
                method: "get",
                url: "/schedules",
            });
            if (res.status === 200) {
                console.log(res.data);
                setEventList(res.data);
                return;
            }
        } catch (error) {
            console.log(error);
        }
    };
    useEffect(() => {
        getAllEvents();
    }, []);

    // 일정 삭제
    const handleEventDelete = async (e) => {
        e.preventDefault();
        if (!window.confirm(`번째 일정을 삭제하시겠습니까?`)) return;
        const eventId = event.id;
        try {
            const res = await axios.delete(`/schedules`, {
                headers:{
                    "Content-Type": "application/json"
                },
                data:{
                    eventId
                }
            });
            if (res.status === 201) {
                // setNewEvent()
                console.log(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <CalendarDiv>
            {/* <h2>캘린더입니다.</h2> */}
            <FullCalendar
                locale="kr"
                plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
                timeZone= "UTC"
                initialView="dayGridMonth"
                headerToolbar={{
                    start: "today prev,next",
                    center: "title",
                    end: "dayGridMonth timeGridWeek"
                }}
                events={eventList}
                dayMaxEvents={true}
                height="90vh"
                expandRows= {true}
                eventClick={(e) => HandleEventClick(e)}
                dateClick={(e) => HandleDateClick(e)}
            />
            {/* 일정클릭 모달 */}
            {eventDetailModal && (
                <Modal>
                    <Overlay
                        // onClick={toggleModal}
                    ></Overlay>
                    <ModalContent>
                        {isEdit ? (
                            <>
                                {/* 에디터컴포넌트 */}
                                <EditEvent isEdit={isEdit} originEvent={event} setEventList={setEventList} toggleIsEdit={toggleIsEdit}/>
                            </>
                        ) : (
                            <>
                                <h2>일정상세</h2>
                                <p>
                                    {/* 일정 번호: {event.id} */}
                                    이름: {event.title}<br />
                                    일시: {event.start.toJSON()}<br />
                                    내용: {event.contents}<br />
                                    장소: {event.place}<br />
                                    카테고리: {event.groupId}
                                </p>
                                <button onClick={toggleIsEdit}>수정</button>
                                <button onClick={handleEventDelete}>삭제</button>
                            </>
                        )}
                        {/* 수정중이 아닐때 close버튼 렌더링 */}
                        { !isEdit && 
                            <CloseModal
                                onClick={toggleModal}
                            >
                                CLOSE
                            </CloseModal>
                        }
                    </ModalContent>
                </Modal>
            )}
            {/* 날짜클릭 모달 */}
            {eventFormModal && (
                <Modal>
                    <Overlay
                        onClick={toggleFormModal}
                    ></Overlay>
                    <ModalContent>
                        <EditEvent isEdit={isEdit} originEvent={event} setEventList={setEventList} toggleIsEdit={toggleIsEdit}/>
                        <CloseModal
                            onClick={toggleFormModal}
                        >
                            CLOSE
                        </CloseModal>
                    </ModalContent>
                </Modal>
            )}
        </CalendarDiv>
    );
};

export default TeamCalender;