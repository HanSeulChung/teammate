import { Routes, Route } from 'react-router-dom';

import Calender from '../views/Calender';
import SignIn from '../components/SignIn';
import SignUp from '../components/SignUp';
import TextEditorView from '../views/TextEditorView';
import MyComponent from '../components/WebSocketTest';

const Router = () => {
    return (
        <Routes>
			{/* <Route path='*' element={<Error />} /> */}
			<Route path='/캘린더' element={<Calender />} />
			<Route path='text-editor' element={<TextEditorView/>}/>
			<Route path='test' element={<MyComponent />} />
		</Routes>
    );
};

export default Router;