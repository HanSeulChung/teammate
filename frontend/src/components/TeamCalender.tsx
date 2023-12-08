import { useState } from "react";
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import '../styles/teamCalender.css'
import { Modal, Overlay, ModalContent, CloseModal, CalendarDiv } from '../styles/TeamCalenderStyled.tsx'
import CreateEvent from "./CreateEvent.tsx";


const TeamCalender = () => {
    // 모달팝업 유무 값
    const [eventDetailModal, setEventDetailModal] = useState(false);
    const [eventFormModal, seteventFormModal] = useState(false);
    
    // 달력 일정
    const [event, setEvent] = useState(``);

    // 모달팝업 관리
    const toggleModal = () => {
        setEventDetailModal(!eventDetailModal);
    };
    const toggleFormModal = () => {
        seteventFormModal(!eventFormModal);
    };

    // 달력 일정 임시데이터
    const events = [
        { title: 'Meeting1', start: new Date('2023-11-29') },
        { title: 'Meeting2', start: new Date('2023-11-30'), description: '백프로팀 회의 하는 날' },
        { title: 'Meeting3', start: new Date('2023-11-30'), description: '화분 물 주는 날' }
    ]

    // 일정클릭 핸들링
    const HandleEventClick = (e) => {
        setEvent({
            title: e.event.title,
            start: e.event.start.toString(),
            description: e.event.extendedProps.description,
        });
        toggleModal();
    }

    // 날짜클릭 핸들링
    const HandleDateClick = (e) => {
        // console.log(e.dayEl);
        toggleFormModal();
    }

    return (
        <CalendarDiv>
            {/* <h2>캘린더입니다.</h2> */}
            <FullCalendar
                locale="kr"
                plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
                initialView="dayGridMonth"
                headerToolbar={{
                    start: "today prev,next",
                    center: "title",
                    end: "dayGridMonth timeGridWeek"
                }}
                events={events}
                eventClick={(e) => HandleEventClick(e)}
                dateClick={(e) => HandleDateClick(e)}
            />
            {/* 일정클릭 모달 */}
            {eventDetailModal && (
                <Modal>
                    <Overlay
                        onClick={toggleModal}
                    ></Overlay>
                    <ModalContent>
                        <h2>일정상세</h2>
                        <p>
                            이름: {event.title}<br />
                            일시: {event.start}<br />
                            내용: {event.description}
                        </p>
                        <button>수정</button>
                        <button>삭제</button>
                        <CloseModal
                            onClick={toggleModal}
                        >
                            CLOSE
                        </CloseModal>
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
                        <CreateEvent />
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