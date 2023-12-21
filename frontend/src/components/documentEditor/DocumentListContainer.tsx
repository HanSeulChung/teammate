import React from "react";
import DocumentList from "./DocumentList";
import { useParams } from "react-router-dom";

interface TextEditorViewProps {}

const DocumentListContainer: React.FC<TextEditorViewProps> = () => {
  const { teamId } = useParams<{ teamId: string }>();

  return <DocumentList teamId={Number(teamId)} />;
};

export default DocumentListContainer;
