import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styled from "styled-components";
import axiosInstance from "../../axios";

const CalendarCategory = () => {
  // 팀 ID
  const { teamId } = useParams();

  // 현재 페이지의 사용자 팀 멤버 Id(participant)
  const [myTeamMemberId, setMyTeamMemberId] = useState<number>();

  // 모달팝업 유무
  const [categoryModal, setCategoryModal] = useState(false);

  const toggleCat = () => {
    setCategoryModal(!categoryModal);
  };

  // 카테고리 목록
  const [categoryList, setCategoryList] = useState([
    {
      categoryId: 1,
      categoryName: "카테고리1",
    },
  ]);
  
  // 카테고리 목록 불러오기
  const getCategoryList = async () => {
    try {
      const res = await axiosInstance({
        method: "get",
        url: `/category/schedule?teamId=${teamId}`,
      });
      if (res.status === 200) {
        console.log("카테고리 목록 -> ", res.data.content);
        setCategoryList(res.data.content);
        return;
      }
    } catch (error) {
      console.log(error);
    }
  };
  
  // 작성자 정보를 위한 현재 팀의 사용자 팀 멤버 id(participant) 가져오기
  useEffect(() => {
    const fetchMyTeamMemberId = async () => {
      try {
        const response = await axiosInstance.get("/member/participants", {});
        const myTeamMemberInfo = response.data.find(
          (item: { teamId: number }) => item.teamId === Number(teamId),
        );
        const authorTeamMemberId = myTeamMemberInfo.teamParticipantsId;      
        setMyTeamMemberId(authorTeamMemberId);
        console.log("작성자 팀멤버 아이디 카테고리-> ",myTeamMemberId);
      } catch (error) {
        console.error("Error fetching participants:", error);
      }
    };
    fetchMyTeamMemberId();
  }, [teamId]);

  useEffect(() => {
    getCategoryList();
    // fetchMyTeamMemberId();
  }, [teamId]);

  // 카테고리 입력 값
  const [categoryInput, setCategoryInput] = useState({
    teamId: teamId,
    createTeamParticipantId: myTeamMemberId,
    categoryName: "",
    categoryType: "schedule",
    color: "",
  });

  // 카테고리 입력값 핸들링
  const handleChangeOption = (e: any) => {
    console.log(e.target.value);
    setCategoryInput({
      ...categoryInput,
      [e.target.name]: e.target.value,
    })
    console.log(categoryInput);
  };

  // 카테고리 추가
  const handleCategoryAdd = async (e: any) => {
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
  };

  // 카테고리 삭제
  const handleCategoryDelete = async (e: any) => {
    try {
      const res = await axiosInstance.delete(`/category`, {
        data: {
          categoryId: e.target.value,
          teamId: teamId,
          participantId: myTeamMemberId,
          isMoved: "",
          newCategoryId: "",
        },
      }
      );
      if (res.status === 200) {
        console.log("카테고리 삭제 성공!! -> ", res);
        getCategoryList();
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
          <button onClick={toggleCat} className="p-3 text-sm font-medium text-gray-600 border-t border-gray-200 rounded-b-lg bg-gray-50 hover:bg-gray-100">
            추가
          </button>
        </div>
        <ul className="h-48 px-3 pb-3  text-sm text-gray-700" aria-labelledby="dropdownSearchButton">
          {categoryList.map((opt) => (
            <li key={opt.categoryId} className="flex items-center p-2 rounded hover:bg-gray-100">
              <input type="checkbox" value="" className="w-4 h-4 checkbox checkbox-success text-green-600 bg-gray-100 border-gray-300 rounded focus:ring-green-50" />
              <label className="w-full ms-2 text-sm font-medium text-gray-900 rounded">{opt.categoryName}</label>
              <button onClick={handleCategoryDelete} value={opt.categoryId} className="w-4 h-4 text-gray-700 border border-gray-200 hover:bg-red-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-red-500 font-medium rounded-lg text-sm p-2.5 text-center inline-flex items-center me-2">
                x
                <span className="sr-only">카테고리 삭제 버튼</span>
              </button>
            </li>
          ))}
        </ul>
      </div>
      {/* 카테고리 추가 버튼 클릭시 모달 */}
      {categoryModal && (
        <Modal>
          <Overlay
            onClick={toggleCat}
          ></Overlay>
          <ModalContent>
            <div className='p-4 md:p-5'>
              <h2 className="text-lg font-semibold text-gray-900">카테고리 추가</h2>
              <CategoryForm>
                <div className="col-span-2">
                </div>
                <label className='block mt-2 mb-2 text-sm font-medium text-gray-900'>카테고리 이름</label>
                <input
                  placeholder='카테고리명'
                  name="categoryName"
                  value={categoryInput.categoryName}
                  onChange={handleChangeOption}
                  className='block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500'
                ></input>
                <label className='block mt-2 mb-2 text-sm font-medium text-gray-900'>색상</label>
                <select
                  name="color"
                  value={categoryInput.color}
                  onChange={handleChangeOption}
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
                onClick={toggleCat}
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