import { useState } from 'react';
import { Modal, Overlay, ModalContent, CloseModal } from '../../styles/TeamCalenderStyled.tsx'
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
    const handleChangeOption = (e: any) => {
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
        <div className="ml-8 w-full mt-16 lg:h-1/2">
            {/* <CategoryUl>
                <h2 className="font-bold text-lg text-center">카테고리</h2>
                {dummyCatList.map((opt) => (
                    <li key={opt.id} className="fc-event border-2 p-1 m-2 w-full rounded-md ml-auto text-center bg-white">
                        <input type="checkbox" />
                        <label>{opt.category}</label>
                    </li>
                ))}
                <button
                    onClick={toggleCat}
                >+</button>
            </CategoryUl> */}
            {/* ------------- */}

            <div className=" bg-white rounded-lg shadow w-60 dark:bg-gray-700">
                <div className="p-3">
                    <div className="relative flex justify-between items-center px-2">
                        <h2 className=''>카테고리</h2>
                        <button onClick={toggleCat} className="p-3 text-sm font-medium text-gray-600 border-t border-gray-200 rounded-b-lg bg-gray-50 dark:border-gray-600 hover:bg-gray-100 dark:bg-gray-700 dark:hover:bg-gray-600 dark:text-gray-500">
                            추가
                        </button>
                    </div>
                </div>
                <ul className="h-48 px-3 pb-3  text-sm text-gray-700 dark:text-gray-200" aria-labelledby="dropdownSearchButton">
                    {dummyCatList.map((opt) => (
                        <li>
                            <div key={opt.id} className="flex items-center p-2 rounded hover:bg-gray-100 dark:hover:bg-gray-600">
                                <input id="checkbox-item-11" type="checkbox" value="" className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-700 dark:focus:ring-offset-gray-700 focus:ring-2 dark:bg-gray-600 dark:border-gray-500" />
                                <label htmlFor="checkbox-item-11" className="w-full ms-2 text-sm font-medium text-gray-900 rounded dark:text-gray-300">{opt.category}</label>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>

            {/* 날짜클릭 모달 */}
            {schdlCtgryModal && (
                <Modal>
                    <Overlay
                        onClick={toggleCat}
                    ></Overlay>
                    <ModalContent>
                        <h2 >카테고리 추가</h2>
                        <CategoryForm>
                            <label>카테고리 이름</label>
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
                            <CommonSubmitBtn
                                onClick={AddOption}
                            >등록</CommonSubmitBtn>
                        </CategoryForm>
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