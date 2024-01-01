import { useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import styled from "styled-components";
import axiosInstance from "../../axios";
import getCategoryList from "./GetCategoryList.tsx";

const CalendarCategory = ({ categoryList, myTeamMemberId, setCategoryList }: any) => {
  // 팀 ID
  const { teamId } = useParams();
  
  // 카테고리 리스트 불러오기
  const getCategoryItems = async(teamId: any) => {
    const response = await getCategoryList(teamId);
    setCategoryList(response);
  } 

  // 추가, 삭제 폼 모달
  const [isAddModal, setIsAddModal] = useState(false);
  const [isDeleteModal, setIsDeleteModal] = useState(false);

  const toggleAddModal = () => {
    setIsAddModal(!isAddModal);
  };
  
  const toggleDeleteModal = (e: any) => {
    setIsDeleteModal(!isDeleteModal);
    
    // 삭제버튼 클릭된 카테고리 id
    setDeleteOption({
      ...deleteOption,
      ['categoryId']: e.target.value,
    })
  };

  // input 요소
  const categoryNameInput = useRef<HTMLInputElement | null>(null);

  // 카테고리 추가 폼 입력값
  const [categoryInput, setCategoryInput] = useState({
    teamId: teamId,
    createTeamParticipantId: myTeamMemberId,
    categoryName: "",
    categoryType: "schedule",
    color: "",
  });
  
  const handleChangeInput = () => {
    // console.log(e.target.value);
    setCategoryInput({
      ...categoryInput,
      // [e.target.name]: e.target.value,
    })
    // console.log(categoryInput);
  };
  
  // 카테고리 삭제 폼 입력값
  const [deleteOption, setDeleteOption] = useState({
    categoryId: 0,
    teamId: teamId,
    participantId: myTeamMemberId,
    isMoved: false,
    newCategoryId: 0,
  });
  
  const handleChangeOption = (e: any) => {
    console.log(e.target.value);
    setDeleteOption({
      ...deleteOption,
      [e.target.name]: e.target.value,
    })
  };
  
  // 카테고리 추가 동작
  const handleCategoryAdd = (e: any) => {
    if(categoryInput.categoryName.length < 1){
      categoryNameInput.current?.focus();
      e.preventDefault();
      return;
    }
    
    onAddCategory();
  };
  
  const onAddCategory = async () => {
    // e.preventDefault();
    try {
      const res = await axiosInstance.post(`/category`, 
      {
        teamId: teamId,
        createParticipantId: myTeamMemberId,
        categoryName: categoryInput.categoryName,
        categoryType: "SCHEDULE",
        color: categoryInput.color,
      }
      );
      if (res.status === 200) {
        console.log("카테고리 옵션이 추가되었습니다 -> ", res);
        // setCategoryList(res.data.content);
        return;
      }
    } catch (error) {
      console.log(error);
    }
  }
  
  // 카테고리 삭제 동작
  const handleCategoryDelete = async () => {
    // e.preventDefault();
    try {
      const res = await axiosInstance.delete(`/category`, {
        data: {
          categoryId: deleteOption.categoryId,
          teamId: teamId,
          participantId: myTeamMemberId,
          isMoved: deleteOption.isMoved,
          newCategoryId: deleteOption.newCategoryId,
        },
      }
      );
      if (res.status === 200) {
        console.log("카테고리 삭제 성공!! -> ", res);
        getCategoryItems(teamId);
        return;
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <div className="p-3 bg-white rounded-lg shadow w-60">
        <div className="relative flex justify-between items-center px-2">
          <h2 className=''>카테고리</h2>
          <button onClick={toggleAddModal} className="p-3 text-sm font-medium text-gray-600 border-t border-gray-200 rounded-b-lg bg-gray-50 hover:bg-gray-100">
            추가
          </button>
        </div>
        <ul className="h-48 px-3 pb-3  text-sm text-gray-700" aria-labelledby="dropdownSearchButton">
          {categoryList.map((opt: any) => (
            <li key={opt.categoryId} className="flex items-center p-2 rounded hover:bg-gray-100">
              <input type="checkbox" value="" className="w-4 h-4 checkbox checkbox-success text-green-600 bg-gray-100 border-gray-300 rounded focus:ring-green-50" />
              <label className="w-full ms-2 text-sm font-medium text-gray-900 rounded">{opt.categoryName}</label>
              <button onClick={toggleDeleteModal} value={opt.categoryId} className="w-4 h-4 text-gray-700 border border-gray-200 hover:bg-red-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-red-500 font-medium rounded-lg text-sm p-2.5 text-center inline-flex items-center me-2">
                x
                <span className="sr-only">카테고리 삭제 버튼</span>
              </button>
            </li>
          ))}
        </ul>
      </div>
      {/* 카테고리 추가 폼 */}
      {isAddModal && (
        <Modal>
          <Overlay
            onClick={toggleAddModal}
          ></Overlay>
          <ModalContent>
            <div className='p-4 md:p-5'>
              <h2 className="text-lg font-semibold text-gray-900">카테고리 추가</h2>
              <CategoryForm>
                <div className="col-span-2">
                </div>
                <label className='block mt-2 mb-2 text-sm font-medium text-gray-900'>카테고리 이름</label>
                <input
                  ref={categoryNameInput}
                  placeholder='카테고리명'
                  name="categoryName"
                  value={categoryInput.categoryName}
                  onChange={handleChangeInput}
                  className='block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
                ></input>
                <label className='block mt-2 mb-2 text-sm font-medium text-gray-900'>색상</label>
                <select
                  name="color"
                  value={categoryInput.color}
                  onChange={handleChangeInput}
                  className='block p-2.5 mb-4 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
                >
                  <option value="#7aac7a">초록</option>
                  <option value="#E21D29">빨강</option>
                  <option value="#336699">파랑</option>
                </select>
                <CommonSubmitBtn
                  onClick={handleCategoryAdd}
                >등록</CommonSubmitBtn>
              </CategoryForm>
              <CloseModal
                onClick={toggleAddModal}
              >
                닫기
              </CloseModal>
            </div>
          </ModalContent>
        </Modal>
      )}
      {/* 카테고리 삭제 폼 */}
      {isDeleteModal && (
        <Modal>
          <Overlay
            // onClick={toggleCat}
          ></Overlay>
          <ModalContent>
            <div className='p-4 md:p-5'>
              <h2 className="text-lg font-semibold text-gray-900">카테고리 삭제</h2>
              <CategoryForm>
                <fieldset>
                  <legend className='block mt-2 mb-2 text-sm font-medium text-gray-900'>일정을 다른 카테고리로 이동</legend>
                  <div className='mb-3'>
                    <input id="isMoved-true" type="radio" onChange={handleChangeOption} name="isMoved" value={true.toString()} className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" />
                    <label htmlFor="isMoved-true" className="text-sm font-medium text-gray-900">
                      예
                    </label>
                    <input checked id="isMoved-false" type="radio" onChange={handleChangeOption} name="isMoved" value={false.toString()} className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500" />
                    <label htmlFor="isMoved-false" className="ms-2 text-sm font-medium text-gray-900">
                      아니오
                    </label>
                  </div>
                </fieldset>
                <fieldset>
                  <label className='block mt-2 mb-2 text-sm font-medium text-gray-900'>이동시킬 카테고리</label>
                  <select
                    name="newCategoryId"
                    value={deleteOption.newCategoryId}
                    onChange={handleChangeOption}
                    className='block p-2.5 mb-4 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
                  >
                    {categoryList.map((item: any) => (
                      <option key={item.categoryId} value={item.categoryId}>
                        {item.categoryName}
                      </option>
                    ))}
                  </select>
                </fieldset>
                <CommonSubmitBtn
                  onClick={handleCategoryDelete}
                >확인</CommonSubmitBtn>
              </CategoryForm>
              <CloseModal
                onClick={toggleDeleteModal}
              >
                닫기
              </CloseModal>
            </div>
          </ModalContent>
        </Modal>
      )}
    </>
  );
};

export default CalendarCategory;

// 스타일드 컴포넌트
export const Modal = styled.div`
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  position: fixed;
  z-index: 99999999;
`

export const Overlay = styled.div`
  background: rgba(49,49,49,0.5);
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  position: fixed;
`

export const ModalContent = styled.div`
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  line-height: 1.4;
  background: white;
  padding: 14px 28px;
  border-radius: 0.5rem;
  max-width: 600px;
  min-width: 300px;
  z-index: 6;
`

export const CloseModal = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 7px;
  background-color: rgb(17 24 39 / var(--tw-text-opacity)); 
`

export const CategoryForm = styled.form`
  display: flex;
  flex-flow: column; 
`

export const CommonSubmitBtn = styled.button`
  background-color: #A3CCA3;
`