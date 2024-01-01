import axiosInstance from "../../axios";

const getCategoryList = async (teamId: any) => {
  try {
    const res = await axiosInstance({
      method: "get",
      url: `/category/schedule?teamId=${teamId}`,
    });
    if (res.status === 200) {
      const categoryList = res.data.content;
      console.log("카테고리 목록 -> ", res.data.content);
      // setCategoryList(res.data.content);
      return categoryList;
    }
  } catch (error) {
    console.log(error);
  }
};

export default getCategoryList;