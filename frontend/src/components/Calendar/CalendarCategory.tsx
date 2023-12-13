import { useState } from 'react';
import { Modal, Overlay, ModalContent, CloseModal} from '../../styles/TeamCalenderStyled.tsx'
import { CategoryUl, CategoryForm } from '../../styles/CalendarCategoryStyled.tsx'
import { CommonSubmitBtn } from '../../styles/CommonStyled.tsx';

const CalendarCategory = () => {
    // 모달팝업 유무
    const [schdlCtgryModal, setSchdlCtgryModal] = useState(false);

    const toggleCat = () => {
        setSchdlCtgryModal(!schdlCtgryModal);
    };
    
    // 더미 카테고리
    const [dummyCatList, setDummyCatList] = useState([
        {
            id: 1,
            category: "카테고리1",
            color: "yellow",
        },
        {
            id: 2,
            category: "카테고리2",
            color: "yellow",
        },
        {
            id: 3,
            category: "카테고리3",
            color: "yellow",
        },
    ]);

    // 카테고리 입력 값
    const [catOption, setCatOption] = useState({
        category: "",
        color: "",
    });

    // 바뀌는값
    const handleChangeOption = (e) => {
        console.log(e.target.value);
        setCatOption({
            ...catOption,
            [e.target.name]: e.target.value,
        })
    };

    const AddOption = () => {
        let optId = 4;
        const newCatOpt = {
            id: optId,
            category: catOption.category,
            color: catOption.color,
        }
        optId += 1;
        setDummyCatList([...dummyCatList, newCatOpt]);
        window.localStorage.setItem("dummyList", JSON.stringify(dummyCatList));
    }
    
    return (
        <div>
            <CategoryUl>
                {dummyCatList.map((opt) => (
                    <li key={opt.id}>
                        <input type="checkbox"/>
                        <label>{opt.category}</label>
                    </li>
                ))}
                <button
                    onClick={toggleCat}
                >+</button>
            </CategoryUl>
            {/* 날짜클릭 모달 */}
            {schdlCtgryModal && (
                <Modal>
                    <Overlay
                        onClick={toggleCat}
                    ></Overlay>
                    <ModalContent>
                    <form>
                        <input 
                            placeholder='카테고리명'
                            name="category"
                            value={catOption.category}
                            onChange={handleChangeOption}
                        ></input>
                        <label>색상</label>
                        <select
                            name="color"
                            value={catOption.color}
                            onChange={handleChangeOption}
                        >
                            <option value="red">red</option>
                            <option value="yellow">yellow</option>
                            <option value="blue">blue</option>
                        </select>
                        <button
                            onClick={AddOption}
                        >등록</button>
                    </form>
                        <CloseModal
                            onClick={toggleCat}
                        >
                            CLOSE
                        </CloseModal>
                    </ModalContent>
                </Modal>
            )}
        </div>
    );
};

export default CalendarCategory;