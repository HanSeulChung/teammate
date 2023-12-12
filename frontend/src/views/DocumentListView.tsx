import React from "react";
import styled from "styled-components";
import DocumentListContainer from "../components/documentEditor/DocumentListContainer";

interface TextEditorViewProps {}

const DocumentListView: React.FC<TextEditorViewProps> = () => {
  return <DocumentListContainer />;
};

export default DocumentListView;
