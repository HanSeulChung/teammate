import { Routes, Route } from 'react-router-dom';
import TextEditorView from '../views/TextEditorView';

const Router = () => {
    return (
        <Routes>
			{/* <Route path='*' element={<Error />} /> */}
			<Route path='/' element={<TextEditorView/>}/>
		</Routes>
    );
};

export default Router;