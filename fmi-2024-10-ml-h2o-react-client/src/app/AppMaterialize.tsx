/**
 * THIS HEADER SHOULD BE KEPT INTACT IN ALL CODE DERIVATIVES AND MODIFICATIONS.
 * 
 * This file provided by IPT is for non-commercial testing and evaluation
 * purposes only. IPT reserves all rights not expressly granted.
 *  
 * The security implementation provided is DEMO only and is NOT intended for production purposes.
 * It is exclusively your responsisbility to seek advice from security professionals 
 * in order to secure the REST API implementation properly.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * IPT BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import './App.css';

import React, { useEffect, useState } from 'react';

import Nav from '../components/Nav/Nav';
import { PostList } from '../components/PostList/PostList';
import { Post } from '../model/post.model';
import PostService from '../service/post-service';
import { StringCallback, PostCallback } from '../shared/shared-types';
import { PostForm } from '../components/PostForm/PostForm';
import { Route, Routes, useNavigate} from 'react-router-dom';
import Login from '../components/Login/Login';
import ProtectedRoute from '../components/ProtectedRoute/ProtectedRoute';
import { FaceRecognition } from '../components/FaceRecognition/FaceRecognition';

// import MOCK_POSTS from './model/mock-posts';
export interface PostAction {
  type: string;
  post: Post;
}

// function postsReducer(state: Post[], action: PostAction) {
//   switch (action.type) {
//     case 'add':
//       return [...state, action.post];
//     // ... other actions ...
//     default:
//       return state;
//   }
// }


function App() {
  const [posts, setPosts] = useState<Post[]>([]);

  const navigate = useNavigate();

  useEffect(() => {
    PostService.getAllPosts().then(
      posts => setPosts(posts)
    );
  }, []);

  // const [posts, dispatch] = useReducer(postsReducer, []);
  // useEffect(() => MOCK_BOOKS.forEach(
  //   (post, index) => setTimeout(dispatch, index*2000, {type: 'add', post: post as Post})),
  //   []); //on mount only


  const handleSearch: StringCallback = async (searchText) => {
    // const foundPosts = await PostService.loadPosts(searchText.split(/[\s,;]+/));
    // console.log(foundPosts);
    // setPosts(foundPosts);
  };


  const handleEditPost: PostCallback = (post) => {
    navigate(`/edit-post/${post.id}`);
  };

  const handleDeletePost: PostCallback = (post) => {
    PostService.deletePost(post.id).then(
      deleted => {
        setPosts(posts.filter(p => p.id !== deleted.id));
        navigate('/posts');
      }
    );
  };


  return (
    <React.Fragment>
      <Nav onSearchPosts={handleSearch} />
      <div className="section no-pad-bot" id="index-banner">
        <div className="container" >
        <Routes>
            <Route index element={<FaceRecognition />} />
            <Route path="/face-recognition" element={<FaceRecognition />} />
            <Route path="/posts" element={<PostList posts={posts} onEditPost={handleEditPost} onDeletePost={handleDeletePost} />} />
            <Route path="/add-post" element={<ProtectedRoute><PostForm /></ProtectedRoute>} />
            <Route path="/edit-post/:postId" element={<PostForm />} />
            <Route path="/login" element={<Login />} />
          </Routes>
        </div>
      </div>
    </React.Fragment>
  );
}




export default App;
