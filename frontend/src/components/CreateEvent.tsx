import {EventInput, EventForm} from '../styles/CreateEventStyled'
import { CommonSubmitBtn } from '../styles/CommonStyled';

const CreateEvent = () => {
    return (
        <EventForm>
            <h2>새 일정 등록</h2>
            <label htmlFor="time">시작시간 끝시간</label>
            <EventInput type="datetime-local" name="time" id="time" />

            <label htmlFor="title">일정제목</label>
            <EventInput type="text" name="title" id="title" />

            <label htmlFor="disc">일정내용</label>
            <EventInput type="text" name="disc" id="disc" />
            
            <label htmlFor="place">일정장소</label>
            <EventInput type="text" name="place" id="place" />
            
            <label htmlFor="repetition">반복</label>
            <select>
                <option value="first">반복 안함</option>
                <option value="second">매일</option>
                <option value="third">매주</option>
            </select>
            
            <label>카테고리</label>
            <select>
                <option value="first">주간회의</option>
                <option value="second">회의</option>
                <option value="third">미팅</option>
            </select>

            <CommonSubmitBtn>등록</CommonSubmitBtn>
        </EventForm>
    );
};

export default CreateEvent;