import {useState} from "react";
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import '../styles/teamCalender.css'

import {Modal, Overlay, ModalContent, CloseModal} from '../styles/TeamCalenderStyled.tsx'

const TeamCalender = () => {
    // 모달팝업 유무
    const [modal, setModal] = useState(false);
    const [event, setEvent] = useState({});

    const toggleModal = () => {
        setModal(!modal);
    };

    // 달력 일정 임시데이터
    const events = [
        { title: 'Meeting1', start: new Date('2023-11-29') },
        { title: 'Meeting2', start: new Date('2023-11-30'), description: '백프로팀 회의 하는 날' }
    ]

    // 일정클릭 핸들링
    const handleEventClick = (e) => {
        // console.log(clickInfo.event.id) // id 값 나옴
        console.log(e.event.title);
        console.log(e.event.start);
        console.log('-------');
        console.log(e.event.extendedProps.description);
        setEvent({
            title: e.event.title,
            start: e.event.start.toString(),
            description: e.event.extendedProps.description,
        });
        toggleModal();
    }

    return (
        <div>
            <h2>캘린더입니다.</h2>
            <FullCalendar
                locale="kr"
                plugins={[ dayGridPlugin]}
                initialView="dayGridMonth"
                headerToolbar ={{
                    start: "today",
                    center: "title",
                    end: "prev,next"
                }}
                events={events}
                eventClick = {(e) => handleEventClick(e)}
                // dateClick
            />
            {/* <button
                onClick={toggleModal}
                className="btn-modal">
                    Open
            </button>
            <p>기존 밑에 깔리는 내용</p> */}
            
            {/* 모달부분 */}
            {modal && (
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
                        <CloseModal
                          onClick={toggleModal}
                        >
                            CLOSE
                        </CloseModal>
                    </ModalContent>
                </Modal>
            )}
        </div>
    );
};

export default TeamCalender;