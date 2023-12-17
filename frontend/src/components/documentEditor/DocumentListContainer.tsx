import React from "react";
import DocumentList from "./DocumentList";

interface TextEditorViewProps {}

const teamId = 1;

const DocumentListContainer: React.FC<TextEditorViewProps> = () => {
  return <DocumentList teamId={teamId} />;
};

export default DocumentListContainer;
